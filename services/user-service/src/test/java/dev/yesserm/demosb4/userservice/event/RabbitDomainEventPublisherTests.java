package dev.yesserm.demosb4.userservice.event;

import dev.yesserm.demosb4.contracts.event.EventMetadata;
import dev.yesserm.demosb4.contracts.event.RabbitMqEventNames;
import dev.yesserm.demosb4.contracts.event.RoleChangedEvent;
import dev.yesserm.demosb4.contracts.event.UserProfileUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RabbitDomainEventPublisherTests {

    @Test
    void publishesUserProfileUpdatedEventToUserExchange() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        RabbitDomainEventPublisher publisher = new RabbitDomainEventPublisher(rabbitTemplate);
        UserProfileUpdatedEvent event = new UserProfileUpdatedEvent(
                UUID.randomUUID(),
                "1",
                Instant.now(),
                new EventMetadata(null, null, null, "user-service"),
                1L,
                "Demo User",
                "user@example.com",
                "+50588887777",
                null,
                Set.of("USER"),
                "user@example.com"
        );

        publisher.publish(event);

        verify(rabbitTemplate).convertAndSend(
                RabbitMqEventNames.USER_EXCHANGE,
                RabbitMqEventNames.USER_PROFILE_UPDATED,
                event
        );
    }

    @Test
    void publishesRoleChangedEventToUserExchange() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        RabbitDomainEventPublisher publisher = new RabbitDomainEventPublisher(rabbitTemplate);
        RoleChangedEvent event = new RoleChangedEvent(
                UUID.randomUUID(),
                "1",
                Instant.now(),
                new EventMetadata(null, null, null, "user-service"),
                1L,
                "user@example.com",
                Set.of("USER"),
                Set.of("ADMIN"),
                "admin@example.com"
        );

        publisher.publish(event);

        verify(rabbitTemplate).convertAndSend(
                RabbitMqEventNames.USER_EXCHANGE,
                RabbitMqEventNames.ROLE_CHANGED,
                event
        );
    }
}
