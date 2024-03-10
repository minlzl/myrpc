package com.lzl.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfo implements Serializable {

    private String appName;
    private String serviceName;
    private String version;
    private String address;
    private Integer port;
}
