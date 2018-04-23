package com.pingcap.tikv.kv.oracle;


import com.pingcap.tikv.PDClient;
import com.pingcap.tikv.meta.TiTimestamp;
import com.pingcap.tikv.util.ConcreteBackOffer;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Future;

/**
 * PdOracle use placement driver client as oracle source.
 */
public class PdOracle implements Oracle {
  private final PDClient client;
  private final Duration updateInterval;

  private long lastTs;

  public PdOracle(PDClient pdClient, Duration updateInterval) {
    this.client = pdClient;
    this.updateInterval = updateInterval;
    this.lastTs = pdClient.getTimestamp(ConcreteBackOffer.newTsoBackOff()).getVersion();
    // TODO: update lastTs every `updateInterval`
  }

  @Override
  public boolean isExpired(long lockTs, long ttl) {
    // TODO: implemnt me
    return false;
  }

  @Override
  public long getTs() {
    TiTimestamp ts = _getTs();
    this.setLastTs(ts.getVersion());
    // TODO: return ts, or lastTs
    return ts.getVersion();
  }

  @Override
  public Future<Long> getTsAsync() {
    return null;
  }

  private void setLastTs(long ts) {
    if (ts > lastTs) {
      this.lastTs = ts;
    }
  }
  private TiTimestamp _getTs() {
    return client.getTimestamp(ConcreteBackOffer.newTsoBackOff());
  }

  @Override
  public void close() throws IOException {
    // TODO: should close the backend thread which will update ts periodically.
  }
}
