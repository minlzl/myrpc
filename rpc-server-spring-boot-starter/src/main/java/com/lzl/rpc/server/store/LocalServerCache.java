package com.lzl.rpc.server.store;

import java.util.HashMap;
import java.util.Map;

public final class LocalServerCache {

    private static final Map<String, Object> serverCacheMap = new HashMap<>();

    public static void store(String serverName, Object server) {
        serverCacheMap.merge(serverName, server, (Object oldObj, Object newObj) -> newObj);
    }

    public static Object get(String serverName) {
        return serverCacheMap.get(serverName);
    }
}
