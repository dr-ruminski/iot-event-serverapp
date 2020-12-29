package com.dr.iot.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dr.iot.model.EventNotification;

/**
 * Configuration class for RabbitMQ message-broker. In future it must be
 * rebuilt. https://docs.spring.io/spring-amqp/reference/html/#_preface
 * 
 * @author dr
 *
 */
@Configuration
public class RabbitMQConfiguration {

	public final static String EVENT_NOTIFICATION_QUEUE_NAME = "queue-event-spring";

	// overwrites properties from application.properties
	// TODO use properties
	@Bean
	public SimpleRabbitListenerContainerFactory batchContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setConnectionTimeout(500);
		factory.setConnectionFactory(cachingConnectionFactory);
		factory.setMessageConverter(new JacksonMessageConverter());
		factory.setBatchListener(true); // configures a BatchMessageListenerAdapter
		factory.setBatchSize(5);
		factory.setConsumerBatchEnabled(true);
		return factory;
	}

	@Bean
	public DefaultClassMapper typeMapper() {
		DefaultClassMapper typeMapper = new DefaultClassMapper();
		typeMapper.setDefaultType(EventNotification.class);
		return typeMapper;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
		jsonMessageConverter.setClassMapper(typeMapper());
		return jsonMessageConverter;
	}

	private class JacksonMessageConverter extends Jackson2JsonMessageConverter {
		public JacksonMessageConverter() {
			super();
		}

		@Override
		public Object fromMessage(Message message) {
			message.getMessageProperties().setContentType("application/json");
			return super.fromMessage(message);
		}
	}

	@Bean
	public Queue queue() {
		return QueueBuilder.durable(EVENT_NOTIFICATION_QUEUE_NAME).build();
	}

}
