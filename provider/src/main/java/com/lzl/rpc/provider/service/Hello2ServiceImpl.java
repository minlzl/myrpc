package com.lzl.rpc.provider.service;

import com.lzl.rpc.api.pojo.Dog;
import com.lzl.rpc.api.service.HelloService;
import com.lzl.rpc.server.annotation.RpcService;

@RpcService(interfaceType = HelloService.class)
public class Hello2ServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return String.format("您好：%s, rpc 调用成功", 9999999);
    }

    @Override
    public Dog sing(Dog dog) {
        return null;
    }
}
