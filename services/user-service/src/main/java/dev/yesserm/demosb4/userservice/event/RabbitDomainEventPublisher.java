package dev.yesserm.demosb4.userservice.event;

import dev.yesserm.demosb4.contracts.event.DomainEvent;
import dev.yesserm.demosb4.contracts.event.RabbitMqEventNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class RabbitDomainEventPublisher implements DomainEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(RabbitDomainEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    RabbitDomainEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMqEventNames.USER_EXCHANGE, event.eventType(), event);
        } catch (AmqpException ex) {
            log.warn("Could not publish event {} for aggregate {}", event.eventType(), event.aggregateId(), ex);
        }
    }
}
