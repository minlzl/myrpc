package com.lzl.core.cache;

import com.lzl.core.common.ServiceInfo;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceInfoCache {

    private static final Map<String, List<ServiceInfo>> serviceInfoMap = new ConcurrentHashMap<>();

    private static final Map<String, Long> serviceTime = new ConcurrentHashMap<>();

    private static final long DEFAULT_DELETE_SECONDS = 60;

    public static void put(String serviceName, List<ServiceInfo> serviceInfos) {
        put(serviceName, serviceInfos, DEFAULT_DELETE_SECONDS);
    }

    public static void put(String serviceName, List<ServiceInfo> serviceInfos, long holeTime) {
        serviceInfoMap.put(serviceName, serviceInfos);
        serviceTime.put(serviceName, System.currentTimeMillis() + holeTime);
    }

    public static List<ServiceInfo> get(String serviceName) {
        if (checkCache(serviceName)) {
            return serviceInfoMap.get(serviceName);
        }
        return null;
    }

    public static void remove(String serviceName) {
        serviceInfoMap.remove(serviceName);
        serviceTime.remove(serviceName);
    }

    public static boolean checkCache(String serviceName) {
        Long time = serviceTime.get(serviceName);
        if (time == null || time == 0L) {
            return false;
        }
        if (time < System.currentTimeMillis()) {
            remove(serviceName);
            return false;
        }
        return true;
    }
}
