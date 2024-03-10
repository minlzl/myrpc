package com.lzl.rpc.client.cache;

import com.lzl.core.common.RpcResponse;
import com.lzl.core.protocol.MessageProtocol;
import com.lzl.rpc.client.transport.RpcFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRpcResponseCache {

    private static final Map<String, RpcFuture<MessageProtocol<RpcResponse>>> requestResponseCache = new ConcurrentHashMap<>();

    public static void add(String reqId, RpcFuture<MessageProtocol<RpcResponse>> future) {
        requestResponseCache.put(reqId, future);
    }

    public static void fillResponse(String reqId, MessageProtocol<RpcResponse> messageProtocol) {
        RpcFuture<MessageProtocol<RpcResponse>> future = requestResponseCache.get(reqId);
        future.setResponse(messageProtocol);
        requestResponseCache.remove(reqId);
    }
}
