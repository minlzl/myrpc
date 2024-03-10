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
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClientStubProxyFactory {

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    public <T> T getProxy(Class<T> clazz, String version, DiscoveryService discoveryService, RpcClientProperties properties) {
        return (T) objectCache.computeIfAbsent(clazz, clz ->
                Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ClientStubInvocationHandler(discoveryService, properties, clz, version))
        );
    }

}
