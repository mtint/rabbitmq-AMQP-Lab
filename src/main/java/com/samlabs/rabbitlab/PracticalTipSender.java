package com.samlabs.rabbitlab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PracticalTipSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(PracticalTipSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 3000L)
    public void sendPracticalTip(){
        PracticalMessage message = new PracticalMessage("Always read the docs",1,false);
        rabbitTemplate.convertAndSend(RabbitlabApplication.TIPS_EXCHANGE_NAME,RabbitlabApplication.ROUTING_KEY,message);
        LOGGER.info("Practical tip message has been sent");
    }
}
