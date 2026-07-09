package dev.yesserm.demosb4.contracts.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID eventId();

    String eventType();

    String aggregateId();

    Instant occurredAt();

    EventMetadata metadata();
}
