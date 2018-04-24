package com.pingcap.tikv.kv.client;

import java.time.Duration;

// Client is a client that sends RPC.
// It should not be used after calling Close().
public interface Client extends AutoCloseable {

  // sends request.
  Response sendRequest(String addr, Request request, Duration timeout);
}
