package com.example.demo.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class DynamicMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        String messageBody = new String(message.getBody());
        String queueName = message.getMessageProperties().getConsumerQueue();
        System.out.println("Received message from queue " + queueName + ": " + messageBody);
    }
}