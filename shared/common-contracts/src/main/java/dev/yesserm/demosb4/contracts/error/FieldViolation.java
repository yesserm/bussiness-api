package dev.yesserm.demosb4.contracts.error;

public record FieldViolation(
        String field,
        String message,
        Object rejectedValue
) {
}
