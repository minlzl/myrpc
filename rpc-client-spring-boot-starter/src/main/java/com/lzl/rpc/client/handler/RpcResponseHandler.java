package com.lzl.rpc.client.handler;

import com.lzl.core.common.RpcResponse;
import com.lzl.core.protocol.MessageProtocol;
import com.lzl.rpc.client.cache.LocalRpcResponseCache;
import com.lzl.rpc.client.common.Constant;
import com.lzl.rpc.client.transport.NettyNetClientPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {

    private volatile static Map<Integer, Set<Channel>> CORE_CHANNELS = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> rpcResponseMessageProtocol) throws Exception {
        String requestId = rpcResponseMessageProtocol.getHeader().getRequestId();
        LocalRpcResponseCache.fillResponse(requestId, rpcResponseMessageProtocol);

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("Client heartbeat monitoring sending... Channel id:{}", ctx.channel().id());
        Channel channel = ctx.channel();
        if (evt instanceof IdleStateEvent) {
            if (channel.isActive()) {
                int poolHash = NettyNetClientPool.getInstance().getPoolHash(channel);
                Set<Channel> channels = CORE_CHANNELS.get(poolHash);
                channels = channels == null ? new HashSet<>(Constant.CORE_CONNECTIONS) : channels;
                channels.add(channel);
                if (channels.stream().filter(Channel::isActive).count() > Constant.CORE_CONNECTIONS) {
                    log.info("Close exist channel beyond CORE_CONNECTIONS: {}", channel.id());
                    channels.remove(channel);
                    channel.close();
                }
                CORE_CHANNELS.put(poolHash, channels);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
