package com.huanshare.streamrabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableBinding(Source.class)
public class StreamRabbitmqApplicationTests {

    @Autowired
    @Qualifier("output")
    MessageChannel output;

    @Test
    public void sendMessage() {
        // 字符串类型发送MQ
        System.out.println("字符串信息发送");
        output.send(MessageBuilder.withPayload("大家好").build());
    }

    @Test
    public void contextLoads() {
    }

}
