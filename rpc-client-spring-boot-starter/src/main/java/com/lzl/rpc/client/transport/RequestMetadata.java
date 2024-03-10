package com.lzl.rpc.client.transport;

import com.lzl.core.common.RpcRequest;
import com.lzl.core.protocol.MessageProtocol;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RequestMetadata implements Serializable {

    private MessageProtocol<RpcRequest> protocol;
    private String address;
    private Integer port;
    private Integer timeout;
}
