package com.pingcap.tikv.kv;

import com.pingcap.tikv.PDClient;
import com.pingcap.tikv.Snapshot;
import com.pingcap.tikv.kv.oracle.Oracle;
import com.pingcap.tikv.kv.oracle.PdOracle;
import com.pingcap.tikv.kv.snapshot.TiKvSnapshot;
import com.pingcap.tikv.kv.txn.TikvTxn;
import com.pingcap.tikv.util.BackOffer;
import com.pingcap.tikv.util.ConcreteBackOffer;
import java.io.IOException;

public class TiKv implements Kv {

  private final Oracle oracle;
  private final RegionCache regionCache;

  public TiKv(PDClient pdClient) {
    this.oracle = new PdOracle(pdClient, java.time.Duration.ofSeconds(10));
    this.regionCache = new RegionCache(pdClient);
  }

  @Override
  public Txn begin() {
    return newTxn();
  }

  @Override
  public Txn beginWithStartTs(long startTs) {
    return null;
  }

  @Override
  public Snapshot snapshot(long version) {
    return null;
  }

  @Override
  public String uuid() {
    return null;
  }

  @Override
  public long currentVersion() {
    return 0;
  }

  @Override
  public Oracle oracle() {
    return this.oracle;
  }

  @Override
  public boolean supportDeleteRange() {
    return false;
  }

  @Override
  public void close() throws IOException {

  }

  private Txn newTxn() {
    BackOffer bo = ConcreteBackOffer.newTsoBackOff();
    long startTs = this.oracle.getTsWithRetry(bo);
    return newTxnWithStartTs(startTs);
  }

  private Txn newTxnWithStartTs(long startTs) {
    TiKvSnapshot snapshot = newTikvSnapshot(Version.of(startTs));
    return new TikvTxn(snapshot, this, startTs);
  }

  private TiKvSnapshot newTikvSnapshot(Version version) {
    return null;
  }

  public RegionCache getRegionCache() {
    return regionCache;
  }
}
