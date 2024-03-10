package com.lzl.rpc.client.transport;

import com.lzl.core.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class NettyNetClientPool {

    private volatile static NettyNetClientPool nettyNetClientPool;
    private final Bootstrap bootstrap = new Bootstrap();
    private volatile static Map<InetSocketAddress, FixedChannelPool> polls = new HashMap<>();

    private NettyNetClientPool () {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(1024 * 2, 1024 * 10, 1024 * 1024));
    }
    public static NettyNetClientPool getInstance() {
        if (nettyNetClientPool == null) {
            synchronized (NettyNetClientPool.class) {
                if (nettyNetClientPool == null) {
                    nettyNetClientPool = new NettyNetClientPool();
                }
            }
        }
        return nettyNetClientPool;
    }
    private FixedChannelPool getFixedChannelPool(InetSocketAddress address) {
        FixedChannelPool fixedChannelPool = polls.get(address);
        if (fixedChannelPool == null) {
            synchronized (address) {
                fixedChannelPool = polls.get(address);
                if (fixedChannelPool == null) {
                    fixedChannelPool = new FixedChannelPool(bootstrap.remoteAddress(address), new NettyChannelPoolHandler(), 2, 2);
                    polls.put(address, fixedChannelPool);
                }
            }
        }
        return fixedChannelPool;
    }

    public Channel getChannel(String requestId, String hostname, int port, int retry) {
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        Channel channel;
        try {
            FixedChannelPool pool = getFixedChannelPool(address);
            Future<Channel> future = pool.acquire();
            channel = future.get();
            AttributeKey<String> REQUEST_ID = AttributeKey.valueOf("requestId");
            channel.attr(REQUEST_ID).set(requestId);
        } catch (ExecutionException e) {
            log.info(e.getMessage());
            if (retry > 0) {
                return getChannel(requestId, hostname, port, retry - 1);
            } else {
                log.error("Peer Server address {} Error", address);
                throw new RpcException("Server Error");
            }
        } catch (Exception e) {
            log.error("Peer Server address {} Error", address);
            throw new RpcException("Server Error");
        }
        return channel;
    }

    public void release(Channel ch, String hostname, int port) {
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        ch.flush();
        polls.get(address).release(ch);
    }

    public int getPoolHash(Channel channel) {
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return System.identityHashCode(polls.get(address));
    }
}
