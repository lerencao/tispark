package com.pingcap.tikv.kv.snapshot;

import com.pingcap.tikv.kv.Snapshot;
import com.pingcap.tikv.kv.TiKv;
import com.pingcap.tikv.kv.Version;
import com.pingcap.tikv.kv.txn.TikvTxn;
import com.pingcap.tikv.kvproto.Kvrpcpb.CommandPri;
import com.pingcap.tikv.kvproto.Kvrpcpb.IsolationLevel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TiKvSnapshot implements Snapshot {
  private final TiKv store;
  private final Version version;

  private IsolationLevel isoLevel;
  private CommandPri priority;
  private boolean notFillCache;
  private boolean syncLog;

  public TiKvSnapshot(TiKv store, Version version) {
    this.store = store;
    this.version = version;
    this.isoLevel = IsolationLevel.SI;
    this.priority = CommandPri.Normal;
  }

  @Override
  public void setPriority(int priority) {
    this.priority = CommandPri.forNumber(priority);
  }


  @Override
  public Map<String, byte[]> batchGet(List<byte[]> keys) {
    return null;
  }

  @Override
  public byte[] get(byte[] key) {

    return new byte[0];
  }

  @Override
  public Iterator<byte[]> seek(byte[] key) {
    return null;
  }

  @Override
  public Iterator<byte[]> seekReverse(byte[] key) {
    return null;
  }
}
