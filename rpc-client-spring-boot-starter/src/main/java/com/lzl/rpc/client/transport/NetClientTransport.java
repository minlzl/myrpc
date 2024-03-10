package com.lzl.rpc.client.transport;

import com.lzl.core.common.RpcResponse;
import com.lzl.core.protocol.MessageProtocol;

public interface NetClientTransport {

    MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception;
}
