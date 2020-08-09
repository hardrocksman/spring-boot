package com.springboot.test.hardrock.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
@Slf4j
public class TestProducer {
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Value("${test.queue}")
	private String testQueue;

	@PostConstruct
	public void init() {
		Thread t = new Thread(new ProducerTask(rabbitTemplate));
		t.start();
	}
}

class ProducerTask implements Runnable {
	RabbitTemplate rabbitTemplate;

	public ProducerTask (RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			String uuid = UUID.randomUUID().toString();

			CorrelationData correlationData = new CorrelationData(uuid);
			rabbitTemplate.convertAndSend("test.queue", (Object) uuid, correlationData);
		}
	}
}
