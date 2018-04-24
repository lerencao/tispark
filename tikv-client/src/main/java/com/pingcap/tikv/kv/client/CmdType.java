package com.pingcap.tikv.kv.client;

public enum CmdType {
  Get(1),
  Scan(2),
  Prewrite(3),
  Commit(4),
  Cleanup(5),
  BatchGet(6),
  BatchRollback(7),
  ScanLock(8),
  ResolveLock(9),
  GC(10),
  DeleteRange(11),

  RawGet(256),
  RawPut(257),
  RawDelete(258),
  RawScan(259),

  Cop(512),
  CopStream(513),

  MvccGetByKey(1024),
  MvccGetByStartTs(1025),
  SplitRegion(1026);
  private final int code;

  CmdType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
