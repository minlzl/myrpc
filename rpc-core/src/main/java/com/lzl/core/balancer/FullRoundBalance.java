package com.lzl.core.balancer;

import com.lzl.core.common.ServiceInfo;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FullRoundBalance implements LoadBalance{

    private int index;

    @Override
    public synchronized ServiceInfo chooseOne(List<ServiceInfo> services) {
        if (index >= services.size()) {
            index = 0;
        }
        return services.get(index++);
    }

    @Override
    public <T> T choose(List<T> list) {
        if (index >= list.size()) {
            index = 0;
        }
        return list.get(index++);
    }
}
