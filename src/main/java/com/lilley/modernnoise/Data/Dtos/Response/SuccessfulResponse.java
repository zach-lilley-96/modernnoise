package com.lilley.modernnoise.Data.Dtos.Response;

public record SuccessfulResponse<T>(
        T data,
        int statusCode
) {
}
