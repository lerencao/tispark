package com.pingcap.tikv.kv.client;

import com.pingcap.tikv.kvproto.Coprocessor;
import com.pingcap.tikv.kvproto.Kvrpcpb.BatchGetResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.BatchRollbackResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.CleanupResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.CommitResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.DeleteRangeResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.GCResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.GetResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.MvccGetByKeyResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.MvccGetByStartTsResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.PrewriteResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawDeleteResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawGetResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawPutResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.RawScanResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.ResolveLockResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.ScanLockResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.ScanResponse;
import com.pingcap.tikv.kvproto.Kvrpcpb.SplitRegionResponse;

public class Response {

  private final CmdType type;

  private GetResponse getResponse;
  private ScanResponse scanResponse;
  private PrewriteResponse prewriteResponse;
  private CommitResponse commitResponse;
  private CleanupResponse cleanupResponse;
  private BatchGetResponse batchGetResponse;
  private BatchRollbackResponse batchRollbackResponse;
  private ScanLockResponse scanLockResponse;
  private ResolveLockResponse resolveLockResponse;
  private GCResponse gcResponse;
  private DeleteRangeResponse deleteRangeResponse;

  private RawGetResponse rawGetResponse;
  private RawPutResponse rawPutResponse;
  private RawDeleteResponse rawDeleteResponse;
  private RawScanResponse rawScanResponse;
  private Coprocessor.Response copResponse;
  private MvccGetByKeyResponse mvccGetByKeyResponse;
  private MvccGetByStartTsResponse mvccGetByStartTsResponse;
  private SplitRegionResponse splitRegionResponse;

  public Response(CmdType type) {
    this.type = type;
  }

  public RawDeleteResponse getRawDeleteResponse() {
    return rawDeleteResponse;
  }

  public Response setRawDeleteResponse(
      RawDeleteResponse rawDeleteResponse) {
    this.rawDeleteResponse = rawDeleteResponse;
    return this;
  }

  public RawScanResponse getRawScanResponse() {
    return rawScanResponse;
  }

  public Response setRawScanResponse(
      RawScanResponse rawScanResponse) {
    this.rawScanResponse = rawScanResponse;
    return this;
  }

  public Coprocessor.Response getCopResponse() {
    return copResponse;
  }

  public Response setCopResponse(Coprocessor.Response copResponse) {
    this.copResponse = copResponse;
    return this;
  }

  public MvccGetByKeyResponse getMvccGetByKeyResponse() {
    return mvccGetByKeyResponse;
  }

  public Response setMvccGetByKeyResponse(
      MvccGetByKeyResponse mvccGetByKeyResponse) {
    this.mvccGetByKeyResponse = mvccGetByKeyResponse;
    return this;
  }

  public MvccGetByStartTsResponse getMvccGetByStartTsResponse() {
    return mvccGetByStartTsResponse;
  }
  // TODO: add cop stream response
  // private

  public Response setMvccGetByStartTsResponse(
      MvccGetByStartTsResponse mvccGetByStartTsResponse) {
    this.mvccGetByStartTsResponse = mvccGetByStartTsResponse;
    return this;
  }

  public SplitRegionResponse getSplitRegionResponse() {
    return splitRegionResponse;
  }

  public Response setSplitRegionResponse(
      SplitRegionResponse splitRegionResponse) {
    this.splitRegionResponse = splitRegionResponse;
    return this;
  }

  public CmdType getType() {
    return type;
  }

  public GetResponse getGetResponse() {
    return getResponse;
  }

  public Response setGetResponse(GetResponse getResponse) {
    this.getResponse = getResponse;
    return this;
  }

  public ScanResponse getScanResponse() {
    return scanResponse;
  }

  public Response setScanResponse(ScanResponse scanResponse) {
    this.scanResponse = scanResponse;
    return this;
  }

  public PrewriteResponse getPrewriteResponse() {
    return prewriteResponse;
  }

  public Response setPrewriteResponse(
      PrewriteResponse prewriteResponse) {
    this.prewriteResponse = prewriteResponse;
    return this;
  }

  public CommitResponse getCommitResponse() {
    return commitResponse;
  }

  public Response setCommitResponse(CommitResponse commitResponse) {
    this.commitResponse = commitResponse;
    return this;
  }

  public CleanupResponse getCleanupResponse() {
    return cleanupResponse;
  }

  public Response setCleanupResponse(
      CleanupResponse cleanupResponse) {
    this.cleanupResponse = cleanupResponse;
    return this;
  }

  public BatchGetResponse getBatchGetResponse() {
    return batchGetResponse;
  }

  public Response setBatchGetResponse(
      BatchGetResponse batchGetResponse) {
    this.batchGetResponse = batchGetResponse;
    return this;
  }

  public BatchRollbackResponse getBatchRollbackResponse() {
    return batchRollbackResponse;
  }

  public Response setBatchRollbackResponse(
      BatchRollbackResponse batchRollbackResponse) {
    this.batchRollbackResponse = batchRollbackResponse;
    return this;
  }

  public ScanLockResponse getScanLockResponse() {
    return scanLockResponse;
  }

  public Response setScanLockResponse(
      ScanLockResponse scanLockResponse) {
    this.scanLockResponse = scanLockResponse;
    return this;
  }

  public ResolveLockResponse getResolveLockResponse() {
    return resolveLockResponse;
  }

  public Response setResolveLockResponse(
      ResolveLockResponse resolveLockResponse) {
    this.resolveLockResponse = resolveLockResponse;
    return this;
  }

  public GCResponse getGcResponse() {
    return gcResponse;
  }

  public Response setGcResponse(GCResponse gcResponse) {
    this.gcResponse = gcResponse;
    return this;
  }

  public DeleteRangeResponse getDeleteRangeResponse() {
    return deleteRangeResponse;
  }

  public Response setDeleteRangeResponse(
      DeleteRangeResponse deleteRangeResponse) {
    this.deleteRangeResponse = deleteRangeResponse;
    return this;
  }

  public RawGetResponse getRawGetResponse() {
    return rawGetResponse;
  }

  public Response setRawGetResponse(RawGetResponse rawGetResponse) {
    this.rawGetResponse = rawGetResponse;
    return this;
  }

  public RawPutResponse getRawPutResponse() {
    return rawPutResponse;
  }

  public Response setRawPutResponse(RawPutResponse rawPutResponse) {
    this.rawPutResponse = rawPutResponse;
    return this;
  }
}
