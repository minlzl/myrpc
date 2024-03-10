package com.lzl.core.balancer;

import com.lzl.core.common.ServiceInfo;

import java.util.List;
import java.util.Random;

public class RandomBalance implements LoadBalance {

    private static Random random = new Random();

    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        return services.get(random.nextInt(services.size()));
    }

    @Override
    public <T> T choose(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
