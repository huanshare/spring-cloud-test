package com.huanshare.serviceRibbon.controller;

import com.huanshare.serviceRibbon.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuhuan on 2018/6/11 17:58.
 */
@Slf4j
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;
    @RequestMapping(value = "/hi")
    public String hi(@RequestParam String name){
        log.info("===call feign-service===");
        return helloService.hiService(name);
    }


}