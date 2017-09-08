/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

/**
 * rabbitmq配置类
 *
 * @author WindsYan
 * @version $Id: RabbitConfig.java, v 0.1 2017年7月13日 下午4:23:54 WindsYan Exp $
 */
@Configuration
public class RabbitConfig {

	public static final String BROADCAST_QUEUE = "broadQueue.B";

	@Value("${spring.rabbitmq.broadcast.exchange_name}")
	private String broadcast_exchange_name;

//	@Value("${spring.rabbitmq.broadcast.routing_key}")
//	private String broadcast_routing_key;

	@Bean
	public Queue broadcast() {
		return new Queue(BROADCAST_QUEUE);
	}

//	@Bean
//	public TopicExchange broadcastExchange() {
//		return new TopicExchange(broadcast_exchange_name);
//	}

	@Bean
	public FanoutExchange broadcastFanoutExchange(){
		return new FanoutExchange(broadcast_exchange_name);
	}

	@Bean
	public Binding bindingExchange(){
		return BindingBuilder.bind(broadcast()).to(broadcastFanoutExchange());
	}

//	@Bean
//	public Binding declareBindingGeneric() {
//		return BindingBuilder.bind(broadcast()).to(broadcastExchange()).with(broadcast_routing_key);
//	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}
}