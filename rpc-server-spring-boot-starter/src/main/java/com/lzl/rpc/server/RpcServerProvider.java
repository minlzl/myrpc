package com.lzl.rpc.server;

import com.lzl.core.common.ServiceInfo;
import com.lzl.core.common.ServiceUtil;
import com.lzl.core.register.RegistryService;
import com.lzl.rpc.server.annotation.RpcService;
import com.lzl.rpc.server.config.RpcServerProperties;
import com.lzl.rpc.server.store.LocalServerCache;
import com.lzl.rpc.server.transport.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import java.net.InetAddress;

@Slf4j
public class RpcServerProvider implements BeanPostProcessor, CommandLineRunner {

    private RegistryService registryService;

    private RpcServerProperties properties;

    private RpcServer rpcServer;

    public RpcServerProvider(RegistryService registryService, RpcServerProperties properties, RpcServer rpcServer) {
        this.registryService = registryService;
        this.properties = properties;
        this.rpcServer = rpcServer;
    }

    @Override
    public void run(String... args) {
        new Thread(()-> rpcServer.start(properties.getPort())).start();
        log.info(" rpc server :{} start, appName :{} , port :{}", rpcServer, properties.getAppName(), properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 关闭之后把服务从ZK上清楚
                registryService.destroy();
            }catch (Exception ex){
                log.error("", ex);
            }

        }));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            try {
                String serviceName = rpcService.interfaceType().getName();
                String version = rpcService.version();
                LocalServerCache.store(ServiceUtil.serviceKey(serviceName, version), bean);

                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(ServiceUtil.serviceKey(serviceName, version));
                serviceInfo.setVersion(version);
                serviceInfo.setPort(properties.getPort());
                serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceInfo.setAppName(properties.getAppName());
                log.info("服务注册成功:{}", serviceInfo);
                registryService.register(serviceInfo);
            } catch (Exception e) {
                log.error("服务注册出错:{}", e);
            }
        }
        return bean;
    }
}
