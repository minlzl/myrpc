package com.lzl.rpc.server.config;

import com.caucho.hessian.io.ExtSerializerFactory;
import com.caucho.hessian.io.SerializerFactory;
import com.lzl.core.register.NacosRegistryService;
import com.lzl.core.register.RegistryService;
import com.lzl.core.serialization.time.LocalDateTimeDeserializer;
import com.lzl.core.serialization.time.LocalDateTimeSerializer;
import com.lzl.rpc.server.RpcServerProvider;
import com.lzl.rpc.server.transport.NettyRpcServer;
import com.lzl.rpc.server.transport.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    @Autowired
    private RpcServerProperties properties;

    @Bean()
    public SerializerFactory serializerFactory() {
        // DO 自定义hessian反序列化
        // step 1. 定义外部序列化工厂
        ExtSerializerFactory extSerializerFactory = new ExtSerializerFactory();

        extSerializerFactory.addSerializer(java.time.LocalDateTime.class,new LocalDateTimeSerializer());
        extSerializerFactory.addDeserializer(java.time.LocalDateTime.class,new LocalDateTimeDeserializer());
        // step 2. 序列化工厂
        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.addFactory(extSerializerFactory);
        return serializerFactory;
    }


//    @Bean
//    @ConditionalOnMissingBean
//    public RegistryService registryService() {
//        return new ZookeeperRegistryService(properties.getRegistryAddr());
//    }

    @Bean(name = "registryService")
    @ConditionalOnMissingBean
    @Primary
    public RegistryService nacosRegistryService() {
        return new NacosRegistryService();
    }

    @Bean
    @ConditionalOnMissingBean(RpcServer.class)
    public RpcServer RpcServer() {
        return new NettyRpcServer();
    }

    @Bean
    @ConditionalOnMissingBean(RpcServerProvider.class)
    public RpcServerProvider rpcServerProvider(@Autowired RegistryService registryService,
                                               @Autowired RpcServer rpcServer,
                                               @Autowired RpcServerProperties rpcServerProperties) {
        return new RpcServerProvider(registryService, rpcServerProperties, rpcServer);
    }
}
