package dev.yesserm.demosb4.authservice.event;

import dev.yesserm.demosb4.contracts.event.EventMetadata;
import dev.yesserm.demosb4.contracts.event.RabbitMqEventNames;
import dev.yesserm.demosb4.contracts.event.UserRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RabbitDomainEventPublisherTests {

    @Test
    void publishesUserRegisteredEventToUserExchange() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        RabbitDomainEventPublisher publisher = new RabbitDomainEventPublisher(rabbitTemplate);
        UserRegisteredEvent event = new UserRegisteredEvent(
                UUID.randomUUID(),
                "1",
                Instant.now(),
                new EventMetadata(null, null, null, "auth-service"),
                1L,
                "Demo User",
                "user@example.com",
                Set.of("USER")
        );

        publisher.publish(event);

        verify(rabbitTemplate).convertAndSend(
                RabbitMqEventNames.USER_EXCHANGE,
                RabbitMqEventNames.USER_REGISTERED,
                event
        );
    }
}
