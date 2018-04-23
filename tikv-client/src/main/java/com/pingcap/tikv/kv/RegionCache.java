package com.pingcap.tikv.kv;

import com.pingcap.tikv.PDClient;
import com.pingcap.tikv.kv.Region.RegionVerId;
import com.pingcap.tikv.kvproto.Metapb;
import com.pingcap.tikv.util.BackOffer;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class RegionCache {

  public static final Duration Rc_Default_Region_Cache_TTL = Duration.ofMinutes(10);
  private final PDClient pdClient;
  private ConcurrentHashMap<RegionVerId, CachedRegion> regions;
  private ConcurrentHashMap<Long, Store> stores;

  public RegionCache(PDClient pdClient) {
    this.pdClient = pdClient;
    this.regions = new ConcurrentHashMap<>();
    this.stores = new ConcurrentHashMap<>();
  }

  // public RpcContext getRpcContext(BackOffer bo, RegionVerId id) {
  //   CachedRegion region = this.regions.get(id);
  //   if (region == null) {
  //     return null;
  //   }
  //
  //   Metapb.Region meta = region.getRegion().meta();
  //   Peer peer = region.getRegion().peer();
  //   String addr = this.getStoreAddr(bo, peer.getStoreId());
  //   if (addr.isEmpty()) {
  //     // Store not found, region must be out of date.
  //     this.dropRegion(id);
  //     return null;
  //   } else {
  //     return RpcContext
  //   }
  // }

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
}
