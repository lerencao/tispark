package com.pingcap.tikv.kv.client;

import com.pingcap.tikv.kvproto.Coprocessor;
import com.pingcap.tikv.kvproto.Kvrpcpb.BatchGetRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.BatchRollbackRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.CleanupRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.CommitRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.Context;
import com.pingcap.tikv.kvproto.Kvrpcpb.Context.Builder;
import com.pingcap.tikv.kvproto.Kvrpcpb.DeleteRangeRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.GCRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.GetRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.MvccGetByKeyRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.MvccGetByStartTsRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.PrewriteRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawDeleteRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawGetRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawPutRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawScanRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.ResolveLockRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.ScanLockRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.ScanRequest;
import com.pingcap.tikv.kvproto.Kvrpcpb.SplitRegionRequest;
import com.pingcap.tikv.kvproto.Metapb.Peer;
import com.pingcap.tikv.kvproto.Metapb.Region;

/**
 * Request wraps all kv/coprocessor requests.
 */
public class Request {

  public final CmdType type;
  public Context.Builder context;

  public GetRequest.Builder getRequest;
  public ScanRequest.Builder scanRequest;
  public PrewriteRequest.Builder prewriteRequest;
  public CommitRequest.Builder commitRequest;
  public CleanupRequest.Builder cleanupRequest;
  public BatchGetRequest.Builder batchGetRequest;
  public BatchRollbackRequest.Builder batchRollbackRequest;
  public ScanLockRequest.Builder scanLockRequest;
  public ResolveLockRequest.Builder resolveLockRequest;
  public GCRequest.Builder gcRequest;
  public DeleteRangeRequest.Builder deleteRangeRequest;

  public RawGetRequest.Builder rawGetRequest;
  public RawPutRequest.Builder rawPutRequest;
  public RawDeleteRequest.Builder rawDeleteRequest;
  public RawScanRequest.Builder rawScanRequest;

  public Coprocessor.Request.Builder copRequest;

  public MvccGetByKeyRequest.Builder mvccGetByKeyRequest;
  public MvccGetByStartTsRequest.Builder mvccGetByStartTsRequest;
  public SplitRegionRequest.Builder splitRegionRequest;


  public Request(CmdType type) {
    this.type = type;
  }

  public CmdType getType() {
    return type;
  }

  // TODO: find how to use context.
  public void setContext(Region region, Peer peer) {
    Builder builder = Context.newBuilder();
    builder.setRegionId(region.getId())
        .setRegionEpoch(region.getRegionEpoch())
        .setPeer(peer);
    switch (this.type) {
      case Get:
        //
    }
  }


}
