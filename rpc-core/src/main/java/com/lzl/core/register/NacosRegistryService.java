package com.lzl.core.register;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.lzl.core.common.ServiceInfo;

import java.io.IOException;

public class NacosRegistryService implements RegistryService {

    NamingService namingService;

    public NacosRegistryService() {
        try {
            namingService = NamingFactory.createNamingService("127.0.0.1:8848");
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        namingService.registerInstance(serviceInfo.getServiceName(), serviceInfo.getAddress(), serviceInfo.getPort());
    }

    @Override
    public void unRegister(ServiceInfo serviceInfo) throws Exception {
        namingService.deregisterInstance(serviceInfo.getAppName(), serviceInfo.getAddress(), serviceInfo.getPort());
    }

    @Override
    public void destroy() throws IOException {
        try {
            namingService.shutDown();
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
