package com.lzl.rpc.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rpc.server")
public class RpcServerProperties {

    private Integer port = 8090;
    /**
     *  服务名称
     */
    private String appName;

    /**
     *  注册中心地址
     */
    private String registryAddr = "127.0.0.1:2181";
}
