package dev.yesserm.demosb4.contracts.error;

import java.time.Instant;
import java.util.List;

public record ApiError(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String traceId,
        Instant timestamp,
        List<FieldViolation> violations
) {

    public ApiError {
        violations = violations == null ? List.of() : List.copyOf(violations);
    }

    public static ApiError of(
            String type,
            String title,
            int status,
            String detail,
            String instance,
            String traceId,
            List<FieldViolation> violations
    ) {
        return new ApiError(type, title, status, detail, instance, traceId, Instant.now(), violations);
    }
}
