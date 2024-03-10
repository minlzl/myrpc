package com.lzl.rpc.provider.service;

import com.lzl.rpc.api.pojo.Dog;
import com.lzl.rpc.api.pojo.History;
import com.lzl.rpc.api.pojo.Home;
import com.lzl.rpc.api.service.TestService;
import com.lzl.rpc.server.annotation.RpcService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@RpcService(interfaceType = TestService.class)
public class TestServiceImpl implements TestService {
    @Override
    public Dog getDog() {
        Dog dog = new Dog();
        dog.setAge(43);
        dog.setName("vfdv");
        Home home = new Home();
        dog.setHome(home);
        dog.setPans(new ArrayList<>(Arrays.asList("cdsc", "cdscdsc")));
        return dog;
    }

    @Override
    public LocalDateTime getTime(LocalDateTime localDateTime) {
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time);
        return time;
    }

    @Override
    public History getHistory() {
        History history = new History();
        history.setContentId(43443L);
        history.setCreateTime(LocalDateTime.now());
        history.setIpAddr("cdscds");
        history.setType("cvsvdf");
        history.setUserId(4343L);
        System.out.println("---" + history);
        return history;
    }
}
