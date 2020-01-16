package com.samlabs.rabbitlab;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitlabApplication {

    private static final String TIPS_EXCHANGE_NAME = "tips_exchange";
    private static final String DEFAULT_PARSING_QUEUE = "default_parsing_que";
    private static final String ROUTING_KEY = "tips_key";

    @Bean
    public TopicExchange tipsExchange(){
    	return  new TopicExchange(TIPS_EXCHANGE_NAME);
	}

	@Bean
	public Queue defaultParsingQueue(){
    	return new Queue(DEFAULT_PARSING_QUEUE);
	}

	@Bean
	public Binding queueToExchangeBinding(){
    	return BindingBuilder.bind(defaultParsingQueue()).to(tipsExchange()).with(ROUTING_KEY);
	}

    public static void main(String[] args) {
        SpringApplication.run(RabbitlabApplication.class, args);
    }

}
