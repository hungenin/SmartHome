package com.homeproject.smarthome.tvGuide.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import java.util.stream.Collectors;

public abstract class HttpResponse {
    public static ResponseEntity<?> invalidDataResponse(BindingResult bindingResult) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + bindingResult.getAllErrors()
                        .stream()
                        .map(HttpResponse::objectErrorToJsonObject)
                        .collect(Collectors.joining(", ")) + "]");
    }

    private static String objectErrorToJsonObject(ObjectError error) {
        return jsonErrorObject(
                error.toString().split("object '")[1].split("'")[0],
                error.toString().split("field '")[1].split("'")[0],
                error.toString().split("rejected value \\[")[1].split("]")[0],
                error.toString().split("default message \\[")[2].split("]")[0]
        );
    }

    private static String jsonErrorObject(String objectName, String fieldName, String rejectedValue, String message) {
        return String.format(
                "{\"object\" : \"%s\", \"field\" : \"%s\", \"rejected value\" : \"%s\", \"message\" : \"%s\"}",
                objectName, fieldName, rejectedValue, message
        );
    }
}
