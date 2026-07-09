package dev.yesserm.demosb4.contracts.event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        String aggregateId,
        Instant occurredAt,
        EventMetadata metadata,
        Long userId,
        String name,
        String email,
        Set<String> roles
) implements DomainEvent {

    public UserRegisteredEvent {
        roles = roles == null ? Set.of() : Set.copyOf(roles);
    }

    @Override
    public String eventType() {
        return RabbitMqEventNames.USER_REGISTERED;
    }
}
