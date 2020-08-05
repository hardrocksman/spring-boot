package org.springboot.test.hardrock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(TestCondition.class)
public class TestConfiguration {
	@Bean
	@Conditional(TestCondition.class)
	public Object testObject() {
		return new Object();
	}
}
