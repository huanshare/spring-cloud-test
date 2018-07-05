package com.huanshare.feignService.controller;

import com.huanshare.feignService.interfaceService.ScheduleServiceHi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuhuan on 2018/6/11 19:02.
 */
@RestController
@Slf4j
public class HiController {
    @Autowired
    ScheduleServiceHi scheduleServiceHi;
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        log.info("===call eureka-client===");
        return scheduleServiceHi.sayHiFromClientOne(name);
    }

}
