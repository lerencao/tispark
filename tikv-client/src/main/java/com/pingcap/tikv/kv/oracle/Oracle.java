package com.pingcap.tikv.kv.oracle;

import com.pingcap.tikv.util.BackOffer;
import java.io.Closeable;
import java.util.concurrent.Future;

public interface Oracle extends Closeable  {
  long getTsWithRetry(BackOffer bo);
  long getTs();
  Future<Long> getTsAsync();
  boolean isExpired(long lockTs, long ttl);
}
