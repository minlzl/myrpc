package com.example.provider2.service;

import com.lzl.rpc.api.service.Hello2Service;
import com.lzl.rpc.server.annotation.RpcService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RpcService(interfaceType = Hello2Service.class)
public class Hello2ServiceImpl implements Hello2Service {
    @Override
    public List<Integer> list(int x) {
        return new ArrayList<>(Arrays.asList(1,4));
    }
}
