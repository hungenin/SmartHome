package com.homeproject.smarthome.tvguide.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import java.util.stream.Collectors;

public abstract class HttpResponse {
    public static ResponseEntity<?> dataNotFoundByIdResponse(String name, Short id) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + jsonErrorObject(name, "id", String.valueOf(id), "Not found!") + "]");
    }

    public static ResponseEntity<?> dataNotFoundByIdResponse(String name, Long id) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + jsonErrorObject(name, "id", String.valueOf(id), "Not found!") + "]");
    }

    public static ResponseEntity<?> dataNotFoundByIdResponse(String jsonObject) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + jsonObject + "]");
    }

    public static ResponseEntity<?> cannotBeDeletedResponse(String name, Long id) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + jsonErrorObject(name, "id", String.valueOf(id), "Not found!") + "]");
    }

    public static ResponseEntity<?> invalidDataResponse(BindingResult bindingResult) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + bindingResult.getAllErrors()
                        .stream()
                        .map(HttpResponse::objectErrorToJsonObject)
                        .collect(Collectors.joining(", ")) + "]");
    }

    public static ResponseEntity<?> invalidDataResponse(String jsonObject) {
        System.out.println(jsonObject);
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("[" + jsonObject + "]");
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
