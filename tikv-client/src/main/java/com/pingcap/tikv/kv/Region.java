package com.pingcap.tikv.kv;

import com.google.protobuf.ByteString;
import com.pingcap.tikv.kvproto.Kvrpcpb.Context;
import com.pingcap.tikv.kvproto.Metapb;
import com.pingcap.tikv.kvproto.Metapb.Peer;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Region {

  private final Metapb.Region meta;
  private Peer peer;
  private Set<Long> unreachableStores;

  public long id() {
    return this.meta.getId();
  }

  public RegionVerId verId() {
    return new RegionVerId(
        meta.getId(),
        meta.getRegionEpoch().getConfVer(),
        meta.getRegionEpoch().getVersion()
    );
  }


  public Metapb.Region meta() {
    return meta;
  }

  public Peer peer() {
    return peer;
  }


  public ByteString startKey() {
    return meta.getStartKey();
  }

  public ByteString endKey() {
    return meta.getEndKey();
  }

  public Context context() {
    return Context.newBuilder()
        .setRegionId(meta.getId())
        .setRegionEpoch(meta.getRegionEpoch())
        .setPeer(peer)
        .build();
  }

  // OnRequestFail records unreachable peer and tries to select another valid peer.
  // It returns false if all peers are unreachable.
  public boolean onRequestFail(long storeId) {
    if (peer.getStoreId() != storeId) {
      return true;
    }

    this.unreachableStores.add(storeId);
    Optional<Peer> reachablePeer = this.meta.getPeersList().parallelStream()
        .filter(p -> {
          return !unreachableStores.contains(p.getStoreId());
        }).findAny();

    if (reachablePeer.isPresent()) {
      this.peer = reachablePeer.get();
      return true;
    } else {
      return false;
    }
  }

  /**
   * switches current peer to the one on specific store. It return false if no peer matches the
   * storeID.
   *
   * @param storeId is leader peer id.
   * @return false if no peers matches the store id.
   */
  public boolean switchPeer(long storeId) {
    List<Peer> peers = meta.getPeersList();
    for (Peer p : peers) {
      if (p.getStoreId() == storeId) {
        this.peer = p;
        return true;
      }
    }
    return false;
  }

  // Contains checks whether the key is in the region, for the maximum region endKey is empty.
  // startKey <= key < endKey.
  public boolean contains(ByteString key) {
    ByteBuffer startKeyByteBuffer = meta.getStartKey().asReadOnlyByteBuffer();
    ByteBuffer endKeyByteBuffer = meta.getEndKey().asReadOnlyByteBuffer();
    ByteBuffer keyByteBuffer = key.asReadOnlyByteBuffer();
    return startKeyByteBuffer.compareTo(keyByteBuffer) <= 0 &&
        (keyByteBuffer.compareTo(endKeyByteBuffer) < 0 || meta.getEndKey().isEmpty());
  }


  public static final class RegionVerId {

    private final long id;
    private final long confVer;
    private final long ver;


    public RegionVerId(long id, long confVer, long ver) {
      this.id = id;
      this.confVer = confVer;
      this.ver = ver;
    }

    public long getVer() {
      return ver;
    }

    public long getConfVer() {
      return confVer;
    }

    public long getId() {
      return id;
    }
  }
}
