package com.lzl.rpc.client.transport;

import com.lzl.core.codec.RpcDecoder;
import com.lzl.core.codec.RpcEncoder;
import com.lzl.core.common.RpcRequest;
import com.lzl.core.common.RpcResponse;
import com.lzl.core.protocol.MessageProtocol;
import com.lzl.rpc.client.cache.LocalRpcResponseCache;
import com.lzl.rpc.client.handler.RpcResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyNetClientTransport implements NetClientTransport{

//    private final Bootstrap bootstrap;
//    private final EventLoopGroup eventLoopGroup;
//    private final RpcResponseHandler handler;

    private NettyNetClientPool nettyNetClientPool;

    public NettyNetClientTransport() {
        nettyNetClientPool = NettyNetClientPool.getInstance();
    }

    @Override
    public MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception {
        MessageProtocol<RpcRequest> protocol = metadata.getProtocol();
        RpcFuture<MessageProtocol<RpcResponse>> future = new RpcFuture<>();
        LocalRpcResponseCache.add(protocol.getHeader().getRequestId(), future);
        Channel channel = nettyNetClientPool.getChannel(protocol.getHeader().getRequestId(), metadata.getAddress(), metadata.getPort(), 3);
        channel.writeAndFlush(protocol);
        nettyNetClientPool.release(channel, metadata.getAddress(), metadata.getPort());
        return metadata.getTimeout() != null ? future.get(metadata.getTimeout(), TimeUnit.MILLISECONDS) : future.get();
    }
}
