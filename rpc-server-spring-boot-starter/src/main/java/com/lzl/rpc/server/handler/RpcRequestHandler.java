package com.lzl.rpc.server.handler;

import com.lzl.core.common.RpcRequest;
import com.lzl.core.common.RpcResponse;
import com.lzl.core.protocol.MessageHeader;
import com.lzl.core.protocol.MessageProtocol;
import com.lzl.core.protocol.MsgStatus;
import com.lzl.core.protocol.MsgType;
import com.lzl.rpc.server.store.LocalServerCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcRequestHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcRequest>> {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcRequest> rpcRequestMessageProtocol) throws Exception {
        threadPoolExecutor.submit(() ->{
            MessageProtocol<RpcResponse> protocol = new MessageProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
            MessageHeader header = rpcRequestMessageProtocol.getHeader();
            header.setMsgType(MsgType.RESPONSE.getType());
            try {
                Object result = handle(rpcRequestMessageProtocol.getBody());
                System.out.println(result);
                rpcResponse.setData(result);
                header.setStatus(MsgStatus.SUCCESS.getCode());
                protocol.setHeader(header);
                protocol.setBody(rpcResponse);
            } catch (Throwable throwable) {
                header.setStatus(MsgStatus.FAIL.getCode());
                rpcResponse.setMessage(throwable.toString());
                System.out.println(throwable);
            }
            channelHandlerContext.writeAndFlush(protocol);
            channelHandlerContext.close();
        });
    }

    private Object handle(RpcRequest rpcRequest) {
        try {
            Object bean = LocalServerCache.get(rpcRequest.getServiceName());
            if (bean == null) {
                throw new RuntimeException(String.format("service not exist: %s !", rpcRequest.getServiceName()));
            }
            Method method = bean.getClass().getMethod(rpcRequest.getMethod(), rpcRequest.getParameterTypes());
            return method.invoke(bean, rpcRequest.getParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
