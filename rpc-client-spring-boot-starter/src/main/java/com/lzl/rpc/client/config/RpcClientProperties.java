package com.lzl.rpc.client.config;

import lombok.Data;

@Data
public class RpcClientProperties {

    private String balance;

    private String serialization = "KRYO";

    private String discoveryAddr = "127.0.0.1:2181";

    private Integer timeout;

}
