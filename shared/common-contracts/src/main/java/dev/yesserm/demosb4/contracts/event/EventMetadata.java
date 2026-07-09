package dev.yesserm.demosb4.contracts.event;

public record EventMetadata(
        String traceId,
        String correlationId,
        String causationId,
        String producer
) {
}
