package com.pingcap.tikv.kv.pd;

import com.google.protobuf.ByteString;
import com.pingcap.tikv.kvproto.Metapb.Region;
import com.pingcap.tikv.kvproto.Metapb.Store;
import com.pingcap.tikv.region.TiRegion;
import com.pingcap.tikv.util.BackOffer;
import java.util.concurrent.Future;

public interface PdClient extends AutoCloseable {

  long getClusterId();

  /*
   * Get Timestamp from Placement Driver
   *
   * @return a timestamp object
   */
  long getTs(BackOffer backOffer);

  /**
   * Get Region from PD by key specified
   *
   * @param key key in bytes for locating a region
   * @return the region whose startKey and endKey range covers the given key
   */
  TiRegion getRegionByKey(BackOffer backOffer, ByteString key);

  Future<TiRegion> getRegionByKeyAsync(BackOffer backOffer, ByteString key);

  /**
   * Get Region by Region Id
   *
   * @param id Region Id
   * @return the region corresponding to the given Id
   */
  Region getRegionByID(BackOffer backOffer, long id);

  Future<Region> getRegionByIDAsync(BackOffer backOffer, long id);

  /**
   * Get Store by StoreId
   *
   * @param storeId StoreId
   * @return the Store corresponding to the given Id
   */
  Store getStore(BackOffer backOffer, long storeId);

  Future<Store> getStoreAsync(BackOffer backOffer, long storeId);
}
