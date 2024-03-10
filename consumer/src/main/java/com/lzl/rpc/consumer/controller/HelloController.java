package com.lzl.rpc.consumer.controller;

import com.lzl.rpc.api.pojo.Dog;
import com.lzl.rpc.api.pojo.History;
import com.lzl.rpc.api.pojo.Home;
import com.lzl.rpc.api.service.Hello2Service;
import com.lzl.rpc.api.service.HelloService;
import com.lzl.rpc.api.service.TestService;
import com.lzl.rpc.client.annotation.RpcAutowired;
import com.lzl.rpc.consumer.mapper.HistoryMapper;
import com.lzl.rpc.consumer.vo.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class HelloController {

    @RpcAutowired
    HelloService helloService;

    @RpcAutowired
    Hello2Service hello2Service;

    @RpcAutowired
    TestService testService;

    @Resource
    HistoryMapper historyMapper;

    @GetMapping("/hello")
    public Resp<String> test1() {
        String name = helloService.sayHello();
        List<History> histories = historyMapper.selectList(null);
        for (History history : histories) {
            System.out.println(history);
        }
        return Resp.success(name);
    }

    @GetMapping("/hello2")
    public Resp<List<Integer>> hello2() {
        List<Integer> list = hello2Service.list(3);
        return Resp.success(list);
    }

    @GetMapping("/test")
    public Resp<Object> test() {
        History history = testService.getHistory();
        System.out.println("controller" + history);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime);
//        LocalDateTime time = testService.getTime(localDateTime);
        return Resp.success(history);
    }

    @PostMapping("/dog")
    public Resp<Dog> dogService(@RequestBody Dog dog) {
//        List<String> s = new ArrayList<>();
//        s.add("hello");
//        s.add("world");
//        Map<String, Integer> map = new HashMap<>();
//        map.put("cds", 43);
//        map.put("cdscs", 5445);
//        Home home = new Home(s, map);
//        Dog dog = new Dog("dog", 23, s, home);
        return Resp.success(helloService.sing(dog));
    }
}
