package dev.yesserm.demosb4.contracts.event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record RoleChangedEvent(
        UUID eventId,
        String aggregateId,
        Instant occurredAt,
        EventMetadata metadata,
        Long userId,
        String email,
        Set<String> previousRoles,
        Set<String> currentRoles,
        String changedBy
) implements DomainEvent {

    public RoleChangedEvent {
        previousRoles = previousRoles == null ? Set.of() : Set.copyOf(previousRoles);
        currentRoles = currentRoles == null ? Set.of() : Set.copyOf(currentRoles);
    }

    @Override
    public String eventType() {
        return RabbitMqEventNames.ROLE_CHANGED;
    }
}
