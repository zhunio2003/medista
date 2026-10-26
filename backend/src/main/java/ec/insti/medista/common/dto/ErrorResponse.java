package ec.insti.medista.common.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        List<FieldErrorDetails> fieldErrors
) {
    public record FieldErrorDetails(String field, String message) {}
}
