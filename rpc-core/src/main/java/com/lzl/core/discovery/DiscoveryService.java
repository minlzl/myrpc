package com.lzl.core.discovery;

import com.lzl.core.common.ServiceInfo;

public interface DiscoveryService {

    ServiceInfo discovery(String serviceName) throws Exception;
}
