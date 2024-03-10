package com.lzl.rpc.client.proxy;

import com.lzl.core.common.RpcRequest;
import com.lzl.core.common.RpcResponse;
import com.lzl.core.common.ServiceInfo;
import com.lzl.core.common.ServiceUtil;
import com.lzl.core.discovery.DiscoveryService;
import com.lzl.core.exception.ResourceNotFoundException;
import com.lzl.core.exception.RpcException;
import com.lzl.core.protocol.MessageHeader;
import com.lzl.core.protocol.MessageProtocol;
import com.lzl.core.protocol.MsgStatus;
import com.lzl.rpc.client.config.RpcClientProperties;
import com.lzl.rpc.client.transport.NetClientTransportFactory;
import com.lzl.rpc.client.transport.RequestMetadata;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ClientStubInvocationHandler implements InvocationHandler {


    private DiscoveryService discoveryService;

    private RpcClientProperties properties;

    private Class<?> clazz;

    private String version;

    public ClientStubInvocationHandler(DiscoveryService discoveryService, RpcClientProperties properties, Class<?> clazz, String version) {
        super();
        this.discoveryService = discoveryService;
        this.properties = properties;
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceInfo serviceInfo = discoveryService.discovery(ServiceUtil.serviceKey(clazz.getName(), version));
        if (serviceInfo == null) {
            throw new ResourceNotFoundException("404");
        }
        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>();
        messageProtocol.setHeader(MessageHeader.build(properties.getSerialization()));
        RpcRequest request = new RpcRequest();
        request.setServiceName(ServiceUtil.serviceKey(clazz.getName(), version));
        request.setMethod(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        messageProtocol.setBody(request);

        MessageProtocol<RpcResponse> responseMessageProtocol = NetClientTransportFactory.getNetClientTransport()
                .sendRequest(
                        RequestMetadata.builder()
                        .protocol(messageProtocol)
                        .address(serviceInfo.getAddress())
                        .port(serviceInfo.getPort())
                        .timeout(properties.getTimeout()).build());
        if (responseMessageProtocol == null) {
            log.error("请求超时");
            throw new RpcException("rpc调用结果失败， 请求超时 timeout:" + properties.getTimeout());
        }

        if (!MsgStatus.isSuccess(responseMessageProtocol.getHeader().getStatus())) {
            log.error("rpc调用结果失败， message：{}", responseMessageProtocol.getBody().getMessage());
            throw new RpcException(responseMessageProtocol.getBody().getMessage());
        }
        return responseMessageProtocol.getBody().getData();
    }
}
