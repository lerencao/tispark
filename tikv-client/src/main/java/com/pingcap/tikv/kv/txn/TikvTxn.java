package com.pingcap.tikv.kv.txn;

import com.pingcap.tikv.Snapshot;
import com.pingcap.tikv.kv.TiKv;
import com.pingcap.tikv.kv.Txn;
import com.pingcap.tikv.kv.snapshot.TiKvSnapshot;
import com.pingcap.tikv.util.BackOffer;
import com.pingcap.tikv.util.BackOffer.BackOffStrategy;

public class TikvTxn implements Txn {
  private final TiKv store;
  private final TiKvSnapshot snapshot;
  private final long startTs;
  private final long startTime;
  public TikvTxn(TiKvSnapshot snapshot, TiKv store, long startTs) {
    this.store = store;
    this.snapshot = snapshot;
    this.startTs = startTs;
    this.startTime = System.currentTimeMillis();
  }
}
