package com.pingcap.tikv.kv.client;

import com.google.common.net.HostAndPort;
import com.pingcap.tikv.kvproto.Kvrpcpb.GetResponse;
import com.pingcap.tikv.kvproto.TikvGrpc.TikvBlockingStub;
import com.pingcap.tikv.kvproto.TikvGrpc.TikvStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class StoreRpcClient implements GRPCClient<TikvBlockingStub, TikvStub>, Client {

  private final Map<String, ManagedChannel> connPool = new ConcurrentHashMap<>();
  private String storeAddr;


  @Override
  public Response sendRequest(String addr, Request request, Duration timeout) {
    ManagedChannel channel = this.getChannel(addr);
    // ClientCalls.blockingUnaryCall(channel, TikvGrpc.METHOD_KV_GET, getBlockingStub().with)
    // Supplier<GetRequest> factory = () ->
    //     GetRequest.newBuilder().setContext(region.getContext()).setKey(key).setVersion(version)
    //         .build();
    //
    // KVErrorHandler<GetResponse> handler =
    //     new KVErrorHandler<>(
    //         regionManager, this, region,
    //         resp -> resp.hasRegionError() ? resp.getRegionError() : null);
    // GetResponse resp = callWithRetry(backOffer, TikvGrpc.METHOD_KV_GET, factory, handler);

    return null;
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public TikvBlockingStub getBlockingStub() {
    return null;
  }

  @Override
  public TikvStub getAsyncStub() {
    return null;
  }

  private Response callRpc(Request request) {
    CmdType cmdType = request.getType();
    Response resp = new Response(cmdType);
    TikvBlockingStub blockingStub = getBlockingStub();
    switch (cmdType) {
      case Get:
        GetResponse response = blockingStub.kvGet(request.getRequest.build());
        resp.setGetResponse(response);
        break;
      case Scan:
        resp.setScanResponse(blockingStub.kvScan(request.scanRequest.build()));
        break;
      case Prewrite:
        resp.setPrewriteResponse(blockingStub.kvPrewrite(request.prewriteRequest.build()));
        break;
      case Commit:
        resp.setCommitResponse(blockingStub.kvCommit(request.commitRequest.build()));
        break;
      case Cleanup:
        resp.setCleanupResponse(blockingStub.kvCleanup(request.cleanupRequest.build()));
        break;
      case BatchGet:
        resp.setBatchGetResponse(blockingStub.kvBatchGet(request.batchGetRequest.build()));
        break;
      case BatchRollback:
        resp.setBatchRollbackResponse(
            blockingStub.kvBatchRollback(request.batchRollbackRequest.build()));
        break;
      case ScanLock:
        resp.setScanLockResponse(blockingStub.kvScanLock(request.scanLockRequest.build()));
        break;
      case ResolveLock:
        resp.setResolveLockResponse(blockingStub.kvResolveLock(request.resolveLockRequest.build()));
        break;
      case GC:
        resp.setGcResponse(blockingStub.kvGC(request.gcRequest.build()));
        break;
      case DeleteRange:
        resp.setDeleteRangeResponse(blockingStub.kvDeleteRange(request.deleteRangeRequest.build()));
        break;
      case RawGet:
        resp.setRawGetResponse(blockingStub.rawGet(request.rawGetRequest.build()));
        break;
      case RawPut:
        resp.setRawPutResponse(blockingStub.rawPut(request.rawPutRequest.build()));
        break;
      case RawDelete:
        resp.setRawDeleteResponse(blockingStub.rawDelete(request.rawDeleteRequest.build()));
        break;
      case RawScan:
        resp.setRawScanResponse(blockingStub.rawScan(request.rawScanRequest.build()));
        break;
      case MvccGetByKey:
        resp.setMvccGetByKeyResponse(
            blockingStub.mvccGetByKey(request.mvccGetByKeyRequest.build()));
        break;
      case MvccGetByStartTs:
        resp.setMvccGetByStartTsResponse(
            blockingStub.mvccGetByStartTs(request.mvccGetByStartTsRequest.build()));
        break;
      default:
        throw new IllegalStateException(String.format("Invalid request type: %s", cmdType.name()));
    }

    return resp;
  }


  public ManagedChannel getChannel(String addressStr) {
    return connPool.compute(addressStr, (key, channel) -> {
      if (channel != null) {
        return channel;
      }
      HostAndPort address;
      try {
        address = HostAndPort.fromString(addressStr);
      } catch (Exception e) {
        throw new IllegalArgumentException("failed to form address");
      }

      // Channel should be lazy without actual connection until first call
      // So a coarse grain lock is ok here
      return ManagedChannelBuilder.forAddress(address.getHostText(), address.getPort())
          .maxInboundMessageSize(1 << 29)
          .usePlaintext(true)
          .idleTimeout(60, TimeUnit.SECONDS)
          .build();
    });
  }
}
