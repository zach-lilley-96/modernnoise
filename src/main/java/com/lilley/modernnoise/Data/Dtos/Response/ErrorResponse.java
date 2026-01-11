package com.lilley.modernnoise.Data.Dtos.Response;

public record ErrorResponse(
        String message,
        int statusCode
) {
}
