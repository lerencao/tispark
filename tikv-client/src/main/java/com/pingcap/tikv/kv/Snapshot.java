package com.pingcap.tikv.kv;

import java.util.List;
import java.util.Map;

public interface Snapshot extends Retriever {
  Map<String, byte[]> batchGet(List<byte[]> keys);
  void setPriority(int priority);
}
