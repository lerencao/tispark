package com.pingcap.tikv.kv.client;

import com.pingcap.tikv.kv.Region.RegionVerId;
import com.pingcap.tikv.kv.RegionCache;
import com.pingcap.tikv.kv.RegionCache.RpcContext;
import com.pingcap.tikv.util.BackOffer;
import java.time.Duration;

// RegionRequestSender sends KV/Cop requests to tikv server. It handles network
// errors and some region errors internally.
//
// Typically, a KV/Cop request is bind to a region, all keys that are involved
// in the request should be located in the region.
// The sending process begins with looking for the address of leader store's
// address of the target region from cache, and the request is then sent to the
// destination tikv server over TCP connection.
// If region is updated, can be caused by leader transfer, region split, region
// merge, or region balance, tikv server may not able to process request and
// send back a RegionError.
// RegionRequestSender takes care of errors that does not relevant to region
// range, such as 'I/O timeout', 'NotLeader', and 'ServerIsBusy'. For other
// errors, since region range have changed, the request may need to split, so we
// simply return the error to caller.
public class RegionRequestSender {

  private String storeAddr;
  private Client client;
  private RegionCache regionCache;

  public RegionRequestSender(RegionCache cache, Client client) {
    this.regionCache = cache;
    this.client = client;
  }

  // TODO: add request, response
  public void sendReq(BackOffer bo, RegionVerId regionVerId, Duration timeout) {
    RpcContext context = this.regionCache.getRpcContext(bo, regionVerId);
    if (context == null) {
      // TODO: fix the return
      return;
    }

    this.storeAddr = context.getAddr();
    this.sendReqToRegion(bo, context, timeout);
  }

  private void sendReqToRegion(BackOffer bo, RpcContext context, Duration timeout) {
  }

}
