package com.pingcap.tikv.kv.pd;

import com.pingcap.tikv.kv.client.GrpcConnPool;
import com.pingcap.tikv.kvproto.PDGrpc;
import com.pingcap.tikv.kvproto.PDGrpc.PDBlockingStub;
import com.pingcap.tikv.kvproto.Pdpb.GetMembersRequest;
import com.pingcap.tikv.kvproto.Pdpb.GetMembersResponse;
import io.grpc.ManagedChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class GRpcPdClient implements PdClient {

  private static final int Max_Init_Cluster_Retries = 100;
  private Collection<String> urls;
  private GrpcConnPool connPool;

  public GRpcPdClient(String pdAddr, GrpcConnPool connPool) {
    this.connPool = connPool;
    this.urls = Arrays.stream(pdAddr.split(","))
        .map(String::trim)
        .collect(Collectors.toList());
    for (int i = 0; i < Max_Init_Cluster_Retries; i++) {
      for (String url : urls) {

      }
    }
  }

  private GetMembersResponse getMember(String url) {
    ManagedChannel channel = this.connPool.getChannel(url);
    PDBlockingStub stub = PDGrpc.newBlockingStub(channel);
    return stub.getMembers(GetMembersRequest.getDefaultInstance());
  }

  long getTs() {

    return 0;
  }
}
