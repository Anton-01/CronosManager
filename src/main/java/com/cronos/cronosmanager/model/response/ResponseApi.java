package com.cronos.cronosmanager.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApi<T> {
    private ZonedDateTime dateTime;
    private int status;
    private String message;
    private T data;

    public ResponseApi(T data, HttpStatus status) {
        this.dateTime = ZonedDateTime.now();
        this.status = status.value();
        this.message = status.getReasonPhrase();
        this.data = data;
    }

    public ResponseApi(HttpStatus status, String message) {
        this.dateTime = ZonedDateTime.now();
        this.status = status.value();
        this.message = message;
    }
}
