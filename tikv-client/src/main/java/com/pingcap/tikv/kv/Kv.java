package com.pingcap.tikv.kv;

import com.pingcap.tikv.Snapshot;
import com.pingcap.tikv.kv.oracle.Oracle;
import java.io.Closeable;

public interface Kv extends Closeable  {

  Txn begin();
  Txn beginWithStartTs(long startTs);
  Snapshot snapshot(long version);
  String uuid();
  long currentVersion();
  Oracle oracle();
  boolean supportDeleteRange();
}
