package com.huanshare.sericeFeign.controller;

import com.huanshare.sericeFeign.interfaceService.ScheduleServiceHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuhuan on 2018/6/11 19:02.
 */
@RestController
public class HiController {
    @Autowired
    ScheduleServiceHi scheduleServiceHi;
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return scheduleServiceHi.sayHiFromClientOne(name);
    }

}
