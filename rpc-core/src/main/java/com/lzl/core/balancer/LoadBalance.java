package com.lzl.core.balancer;

import com.lzl.core.common.ServiceInfo;

import java.util.List;

public interface LoadBalance {

    ServiceInfo chooseOne(List<ServiceInfo> services);

    <T> T choose(List<T> list);
}
