package com.huanshare.streamrabbitmq;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableBinding({Processor.class, OrderProcessor.class, ProductProcessor.class})
public class StreamRabbitmqApplication implements CommandLineRunner {

    @Autowired
    @Qualifier("output")
    MessageChannel output;

    @Autowired
    @Qualifier("outputOrder")
    MessageChannel outputOrder;

    @Autowired
    ProductProcessor productProcessor;
    static {
        Gson gson=new Gson();
    }

    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitmqApplication.class);
    }


    // 监听 binding 为 Processor.INPUT 的消息
    @StreamListener(Processor.INPUT)
    public void input(Message<String> message) {
        System.out.println("监听收到：" + message.getPayload());
    }

    // 监听 binding 为 OrderIntf.INPUT_ORDER 的消息
    @StreamListener(OrderProcessor.INPUT_ORDER)
    public void inputOrder(Order order) {
        System.out.println("=====订单监听收到=====");
        System.out.println("订单编号：" + order.getOrderNum());
        System.out.println("订单类型：" + order.getType());
        System.out.println("订单数量：" + order.getNum());
        System.out.println("=====订单处理完成=====");
    }


    @StreamListener(ProductProcessor.INPUT_PRODUCT_ADD)
    public void inputProductAdd(Message<String> message) {

        System.out.println("新增产品监听收到：" + message.getPayload());
    }


    @Override
    public void run(String... strings) throws Exception {
        // 字符串类型发送MQ
        System.out.println("字符串信息发送");
        output.send(MessageBuilder.withPayload("大家好").build());

        // 使用 定义的接口的方式来发送
        System.out.println("新增产品发送");
        productProcessor.outputProductAdd().send(MessageBuilder.withPayload("新增产品发送").build());

        // 实体类型发送MQ
        System.out.println("订单实体发送");
        Order appleOrder = new Order();
        appleOrder.setOrderNum("0000001");
        appleOrder.setNum(10);
        appleOrder.setType("APPLE");
        appleOrder.setCreateAt(new Date());
        // 使用 注入 MessageChannel 方式发送
        outputOrder.send(MessageBuilder.withPayload(appleOrder).build());
    }


    // 定时轮询发送消息到 binding 为 Processor.OUTPUT
    /*@Bean
    @InboundChannelAdapter(value = Processor.OUTPUT, poller = @Poller(fixedDelay = "3000", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        String msg="短消息-" + new Date();
        System.out.println("发送短消息"+msg);
        return () -> MessageBuilder.withPayload(msg).build();
    }*/

    @Scheduled(fixedRate = 3000) // 每1分钟执行一次
    public void getToken() {
        String msg="发送短消息-" + new Date();
        System.out.println(msg);
        output.send(MessageBuilder.withPayload(msg).build());
    }


}

interface OrderProcessor {

    String INPUT_ORDER = "inputOrder";
    String OUTPUT_ORDER = "outputOrder";

    @Input(INPUT_ORDER)
    SubscribableChannel inputOrder();

    @Output(OUTPUT_ORDER)
    MessageChannel outputOrder();
}


interface ProductProcessor {

    String INPUT_PRODUCT_ADD = "inputProductAdd";
    String OUTPUT_PRODUCT_ADD = "outputProductAdd";

    @Input(INPUT_PRODUCT_ADD)
    SubscribableChannel inputProductAdd();

    @Output(OUTPUT_PRODUCT_ADD)
    MessageChannel outputProductAdd();

}

@Data
class Order {

    private String orderNum;

    private String type;

    private int num;

    private Date createAt;
}