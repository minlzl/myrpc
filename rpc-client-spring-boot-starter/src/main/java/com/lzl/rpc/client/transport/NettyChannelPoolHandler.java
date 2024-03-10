package com.lzl.rpc.client.transport;

import com.lzl.core.codec.RpcDecoder;
import com.lzl.core.codec.RpcEncoder;
import com.lzl.rpc.client.handler.RpcResponseHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyChannelPoolHandler extends AbstractChannelPoolHandler {
    @Override
    public void channelCreated(Channel channel) throws Exception {
        log.info("Create channel. Channel id: " + channel.id() +"Channel REAL HASH: " + System.identityHashCode(channel));
        SocketChannel socketChannel = (SocketChannel) channel;
        socketChannel.config().setKeepAlive(true);
        socketChannel.config().setTcpNoDelay(true);
        socketChannel.pipeline()
                .addLast(new RpcDecoder())
                .addLast(new RpcResponseHandler())
                .addLast(new RpcEncoder<>());
    }

    @Override
    public void channelReleased(Channel ch) throws Exception {
        ch.writeAndFlush(Unpooled.EMPTY_BUFFER);
        log.info("Released channel. Channel ID: " + ch.id());
    }
}
