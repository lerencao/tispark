package com.pingcap.tikv.kv.client;

import com.google.common.net.HostAndPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GrpcConnPool {

  private final Map<String, ManagedChannel> connPool;

  private GrpcConnPool(int initialCapacity) {
    this.connPool = new HashMap<>(initialCapacity);
  }

  public static GrpcConnPool create(int initialCapacity) {
    return new GrpcConnPool(initialCapacity);
  }

  public synchronized ManagedChannel getChannel(String addressStr) {
    ManagedChannel channel = connPool.get(addressStr);
    if (channel == null) {
      HostAndPort address;
      try {
        address = HostAndPort.fromString(addressStr);
      } catch (Exception e) {
        throw new IllegalArgumentException("failed to form address");
      }

      // Channel should be lazy without actual connection until first call
      // So a coarse grain lock is ok here
      channel = ManagedChannelBuilder.forAddress(address.getHostText(), address.getPort())
          .maxInboundMessageSize(1 << 29)
          .usePlaintext(true)
          .idleTimeout(60, TimeUnit.SECONDS)
          .build();
      connPool.put(addressStr, channel);
    }
    return channel;
  }
}
