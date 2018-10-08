package com.huanshare.redisRateLimiter;

import com.huanshare.redisRateLimiter.redis.RateLimiter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RedisRateLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisRateLimiterApplication.class, args);
    }

    @RateLimiter(limit = 2, timeout = 20000)
    @RequestMapping("/hi")
    public String home(@RequestParam String name) {
        return name;
    }

    @RateLimiter(limit = 1, timeout = 20000)
    @RequestMapping("/test")
    public String test() {
        return "3432423";
    }
}
