package com.pingcap.tikv.kv;

import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.protobuf.ByteString;
import com.pingcap.tikv.PDClient;
import com.pingcap.tikv.key.Key;
import com.pingcap.tikv.kv.Region.RegionVerId;
import com.pingcap.tikv.kvproto.Metapb;
import com.pingcap.tikv.kvproto.Metapb.Peer;
import com.pingcap.tikv.region.TiRegion;
import com.pingcap.tikv.util.BackOffer;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RegionCache caches Regions loaded from PD.
 *
 * It's thread-safe.
 */
public class RegionCache {

  public static final Duration Rc_Default_Region_Cache_TTL = Duration.ofMinutes(10);
  private final PDClient pdClient;
  private final ConcurrentHashMap<RegionVerId, CachedRegion> regions;
  private final RangeMap<Key, Long> keyToRegionIdCache;
  private final ConcurrentHashMap<Long, Store> stores;

  public RegionCache(PDClient pdClient) {
    this.pdClient = pdClient;
    this.regions = new ConcurrentHashMap<>();
    this.stores = new ConcurrentHashMap<>();
    this.keyToRegionIdCache = TreeRangeMap.create();
  }

  public synchronized KeyLocation locateKey(BackOffer bo, ByteString key) {
    Key rawKey = Key.toRawKey(key);
    Long regionId = keyToRegionIdCache.get(rawKey);
    if (regionId == null) {
      TiRegion region = pdClient.getRegionByKey(bo, key);
      regions.put(region.getId(), new CachedRegion(region.getMeta(), System.currentTimeMillis()));
    }

  }

  public RpcContext getRpcContext(BackOffer bo, RegionVerId id) {
    CachedRegion region = this.regions.get(id);
    if (region == null) {
      return null;
    }

    Metapb.Region meta = region.getRegion().meta();
    Peer peer = region.getRegion().peer();
    String addr = this.getStoreAddr(bo, peer.getStoreId());
    if (addr.isEmpty()) {
      // Store not found, region must be out of date.
      this.dropRegion(id);
      return null;
    } else {
      return new RpcContext(id, meta, peer, addr);
    }
  }

  void dropRegion(RegionVerId regionVerId) {
    this.regions.remove(regionVerId);
  }

  public String reloadStoreAddr(BackOffer bo, long storeId) {
    String addr = this.loadStoreAddr(bo, storeId);
    if (addr.isEmpty()) {
      return addr;
    }

    this.stores.put(storeId, new Store(storeId, addr));
    return addr;
  }

  private String loadStoreAddr(BackOffer bo, long storeId) {
    Metapb.Store store = this.pdClient.getStore(bo, storeId);
    if (store == null) {
      return "";
    } else {
      return store.getAddress();
    }
  }

  String getStoreAddr(BackOffer bo, long storeId) {
    Store store = this.stores.get(storeId);
    if (store != null) {
      return store.addr;
    } else {
      return this.reloadStoreAddr(bo, storeId);
    }
  }

  public static class RpcContext {

    private final RegionVerId region;
    private final Metapb.Region meta;
    private final Peer peer;
    private final String addr;

    public RpcContext(RegionVerId region, Metapb.Region meta,
        Peer peer, String addr) {
      this.region = region;
      this.meta = meta;
      this.peer = peer;
      this.addr = addr;
    }

    public Metapb.Region getMeta() {
      return meta;
    }

    public RegionVerId getRegion() {
      return region;
    }

    public String getAddr() {
      return addr;
    }

    public Peer getPeer() {
      return peer;
    }
  }

  public static final class Store {

    private final long id;
    private final String addr;

    public Store(long id, String addr) {
      this.id = id;
      this.addr = addr;
    }

    public long getId() {
      return id;
    }

    public String getAddr() {
      return addr;
    }
  }

  public static class CachedRegion {

    private final Region region;
    private long lastAccess;


    public CachedRegion(Region region, long lastAccess) {
      this.region = region;
      this.lastAccess = lastAccess;
    }

    public Region getRegion() {
      return region;
    }

    boolean isValid() {
      long last = this.lastAccess;

      return System.currentTimeMillis() - last < Rc_Default_Region_Cache_TTL.toMillis();
    }
  }

  public static class KeyLocation {

    private final RegionVerId regionVerId;
    private final ByteString startKey;
    private final ByteString endKey;

    public KeyLocation(RegionVerId regionVerId, ByteString startKey,
        ByteString endKey) {
      this.regionVerId = regionVerId;
      this.startKey = startKey;
      this.endKey = endKey;
    }

    public RegionVerId getRegionVerId() {
      return regionVerId;
    }

    public ByteString getStartKey() {
      return startKey;
    }

    public ByteString getEndKey() {
      return endKey;
    }
  }
}
