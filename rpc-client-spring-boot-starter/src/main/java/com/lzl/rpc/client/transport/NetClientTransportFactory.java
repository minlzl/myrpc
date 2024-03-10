package com.lzl.rpc.client.transport;

public class NetClientTransportFactory {

    public static NetClientTransport getNetClientTransport() {
        return new NettyNetClientTransport();
    }
}
