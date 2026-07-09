package dev.yesserm.demosb4.contracts.event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserProfileUpdatedEvent(
        UUID eventId,
        String aggregateId,
        Instant occurredAt,
        EventMetadata metadata,
        Long userId,
        String name,
        String email,
        String phone,
        String avatar,
        Set<String> roles,
        String updatedBy
) implements DomainEvent {

    public UserProfileUpdatedEvent {
        roles = roles == null ? Set.of() : Set.copyOf(roles);
    }

    @Override
    public String eventType() {
        return RabbitMqEventNames.USER_PROFILE_UPDATED;
    }
}
