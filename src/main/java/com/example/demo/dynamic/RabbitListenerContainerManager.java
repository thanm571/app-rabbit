package com.example.demo.dynamic;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RabbitListenerContainerManager {

    private final Map<String, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    public void createAndStartListener(String queueName, MessageListener listener, SimpleRabbitListenerContainerFactory factory) {
        containers.computeIfAbsent(queueName, q -> {
            SimpleMessageListenerContainer container = factory.createListenerContainer();
            container.setQueueNames(q);
            container.setMessageListener(listener);
            container.start();
            System.out.println("Started listener for queue: " + q);
            return container;
        });
    }

    public void stopListener(String queueName) {
        SimpleMessageListenerContainer container = containers.remove(queueName);
        if (container != null) {
            container.stop();
            System.out.println("Stopped listener for queue: " + queueName);
        }
    }
}
