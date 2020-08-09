package com.springboot.test.hardrock.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class TestConsumer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(queues = "test.queue")
    public void consume(Message message, Channel channel) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        try {
//            SettlePrepareDataBuilder.SettlePrepareData data = SettlePrepareDataBuilder.SettlePrepareData.parseFrom(message.getBody());
            long start = System.currentTimeMillis();

            String correlationId = getCorrelationId(message);
//            log.info("get message correlationId:[{}]", correlationId);

            String messageStr = new String(message.getBody());

            log.info("get message:[{}]", messageStr);

            channel.basicAck(tag, false);
            log.info("consumer message take:[{}]ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("process message error:[{}]", e);
            channel.basicNack(tag, false, false);
        }
    }

    /**
     * 获取CorrelationId
     *
     * @param message
     * @return
     */
    private String getCorrelationId(Message message) {
        String correlationId = null;

        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();

//		headers.forEach((key, value) -> {
//			if (key.equals("spring_returned_message_correlation")) {
//				correlationId = value;
//			}
//		});

        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.equals("spring_returned_message_correlation")) {
                correlationId = value;
            }
        }

        return correlationId;
    }
}
