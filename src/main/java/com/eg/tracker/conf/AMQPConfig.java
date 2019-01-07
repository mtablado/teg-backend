package com.eg.tracker.conf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AMQPConfig {

	public static final String topicExchangeName = "teg-traffic";

    static final String queueName = "/amq/queue/teg-traffic-queue";

    static final String routingKey = "teg.#";
    static final String trafficPositionKey = "teg.traffic.position";

    /**
     * Per-Queue message time-to-live. Default value, 20s.
     */
    @Value("${eg.x-message-ttl:20000}")
    private long messageTtl;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    Queue queue() {
    	Map<String, Object> args = new HashMap<>();
    	// Messages time-to-live
    	args.put("x-message-ttl", this.messageTtl);
        return new Queue(queueName, false, false, false, args);
    }

	public static String trafficPositionKey() {
		return routingKey;
	}

	@Bean(name="trafficExchange")
	public Exchange exchange() {
		return ExchangeBuilder
				.topicExchange(topicExchangeName)
				.durable(true)
				.build();
	}

	@PostConstruct
	public void setupTopicDestinations() {
		this.amqpAdmin.declareExchange(this.exchange());
	}

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    MessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(this.logListener());
        return container;
    }

    MessageListener logListener() {
    	return message -> log.debug("Message received:" + message);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    	return new RabbitTemplate(connectionFactory);
    }


}
