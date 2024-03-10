package com.lzl.rpc.client.config;

import com.lzl.core.balancer.FullRoundBalance;
import com.lzl.core.balancer.LoadBalance;
import com.lzl.core.balancer.RandomBalance;
import com.lzl.core.discovery.DiscoveryService;
import com.lzl.core.discovery.NacosDiscoveryService;
import com.lzl.rpc.client.processor.RpcClientProcessor;
import com.lzl.rpc.client.proxy.ClientStubProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class RpcClientAutoConfiguration {


    @Bean
    public RpcClientProperties rpcClientProperties(Environment env) {
        BindResult<RpcClientProperties> result = Binder.get(env).bind("rpc.client", RpcClientProperties.class);
        return result.get();
    }


    @Bean
    @ConditionalOnMissingBean
    public ClientStubProxyFactory clientStubProxyFactory() {
        return new ClientStubProxyFactory();
    }

    @Primary
    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "balance", havingValue = "randomBalance", matchIfMissing = true)
    public LoadBalance randomBalance() {
        return new RandomBalance();
    }

    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "balance", havingValue = "fullRoundBalance")
    public LoadBalance loadBalance() {
        return new FullRoundBalance();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean({RpcClientProperties.class, LoadBalance.class})
//    public DiscoveryService discoveryService(@Autowired RpcClientProperties properties,
//                                             @Autowired LoadBalance loadBalance) {
//        return new ZookeeperDiscoveryService(properties.getDiscoveryAddr(), loadBalance);
//    }

//    @Primary
    @Bean(name = "discoveryService")
    @ConditionalOnMissingBean
    @ConditionalOnBean({RpcClientProperties.class, LoadBalance.class})
    public DiscoveryService nacosDiscoveryService(@Autowired LoadBalance loadBalance) {
        return new NacosDiscoveryService(loadBalance);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientProcessor rpcClientProcessor(@Autowired ClientStubProxyFactory clientStubProxyFactory,
                                                 @Autowired DiscoveryService discoveryService,
                                                 @Autowired RpcClientProperties properties) {
        return new RpcClientProcessor(clientStubProxyFactory, discoveryService, properties);
    }
}
