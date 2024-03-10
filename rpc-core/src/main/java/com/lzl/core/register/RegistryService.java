package com.lzl.core.register;

import com.lzl.core.common.ServiceInfo;

import java.io.IOException;

public interface RegistryService {
    void register(ServiceInfo serviceInfo) throws Exception;

    void unRegister(ServiceInfo serviceInfo) throws Exception;

    void destroy() throws IOException;
}
