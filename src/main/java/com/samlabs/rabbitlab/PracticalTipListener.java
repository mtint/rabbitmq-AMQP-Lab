package com.samlabs.rabbitlab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PracticalTipListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticalTipListener.class);

    @RabbitListener(queues = {RabbitlabApplication.DEFAULT_PARSING_QUEUE})
    public void consumeDefaultMessage(final PracticalMessage message){
        LOGGER.info("Received message with default configuration:{}", message);
    }
}
