package com.lzl.core.discovery;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lzl.core.balancer.LoadBalance;
import com.lzl.core.cache.ServiceInfoCache;
import com.lzl.core.common.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class NacosDiscoveryService implements DiscoveryService {

    public static final String VERSION = "1.0";

    LoadBalance loadBalance;

    public static NamingService namingService;

    public NacosDiscoveryService(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        if (ServiceInfoCache.get(serviceName) != null) {
            List<ServiceInfo> serviceInfos = ServiceInfoCache.get(serviceName);
            return loadBalance.chooseOne(serviceInfos);
        }
        if (namingService == null) {
            namingService = NamingFactory.createNamingService("127.0.0.1:8848");
        }
        List<Instance> allInstances = namingService.getAllInstances(serviceName);
        if (CollectionUtils.isEmpty(allInstances)) {
            return null;
        }
        List<ServiceInfo> serviceInfos = new ArrayList<>();
        for (Instance instance : allInstances) {
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setAddress(instance.getIp());
            serviceInfo.setAppName(instance.getServiceName());
            serviceInfo.setServiceName(instance.getServiceName());
            serviceInfo.setPort(instance.getPort());
            serviceInfo.setVersion(VERSION);
            serviceInfos.add(serviceInfo);
        }
        ServiceInfoCache.put(serviceName, serviceInfos);
        return discovery(serviceName);
    }
}
