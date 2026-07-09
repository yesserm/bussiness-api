package dev.yesserm.demosb4.authservice.event;

import dev.yesserm.demosb4.contracts.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
