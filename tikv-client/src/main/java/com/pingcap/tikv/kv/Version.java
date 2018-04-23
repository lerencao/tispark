package com.pingcap.tikv.kv;


public class Version implements Comparable<Version> {
  public static Version of(long startTs) {
    return new Version(startTs);
  }
  private final long ver;
  private Version(long ver) {
    this.ver = ver;
  }

  public long getVer() {
    return ver;
  }

  @Override
  public int compareTo(Version o) {
    return Long.compare(ver, o.ver);
  }
}
