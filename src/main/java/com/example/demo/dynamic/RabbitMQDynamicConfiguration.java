package com.example.demo.dynamic;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.entity.QueueConfig;
import com.example.demo.listener.DynamicMessageListener;
import com.example.demo.repository.QueueConfigRepository;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
public class RabbitMQDynamicConfiguration {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private QueueConfigRepository queueConfigRepository;

    @Autowired
    private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @Autowired
    private RabbitListenerContainerManager containerManager;

    @PostConstruct
    public void setupDynamicListeners() {
        loadAndCreateRabbitMQComponents();
    }

    @Scheduled(fixedRate = 60000) // Poll the database every 60 seconds for new configurations
    public void scheduledLoad() {
        loadAndCreateRabbitMQComponents();
    }

    private void loadAndCreateRabbitMQComponents() {
        List<QueueConfig> configs = queueConfigRepository.findAll();
        for (QueueConfig config : configs) {
            String queueName = config.getQueueName();
            String exchangeName = config.getExchangeName();
            String routingKey = config.getRoutingKey();

            // Declare components
            Queue queue = new Queue(queueName, true, false, false);
            DirectExchange exchange = new DirectExchange(exchangeName);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);

            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareExchange(exchange);
            rabbitAdmin.declareBinding(binding);

            // Create and start listener if it doesn't exist
            containerManager.createAndStartListener(queueName, new DynamicMessageListener(), rabbitListenerContainerFactory);
        }
    }
}