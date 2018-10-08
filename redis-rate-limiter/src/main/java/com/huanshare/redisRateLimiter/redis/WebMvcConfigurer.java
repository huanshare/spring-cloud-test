package com.huanshare.redisRateLimiter.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by liuhuan on 2018/10/8 14:05.
 */
@Slf4j
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);

                if (rateLimiter != null) {
                    int limit = rateLimiter.limit();
                    int timeout = rateLimiter.timeout();
                    Jedis jedis = jedisPool.getResource();
                    String token = RedisRateLimiter.acquireTokenFromBucket(jedis, method.getName(), limit, timeout);
                    if (token == null) {
                        response.sendError(500);
                        return false;
                    }
                    log.debug("token -> {}", token);
                    jedis.close();
                }
                return true;
            }
        }).addPathPatterns("/*");
    }
}
