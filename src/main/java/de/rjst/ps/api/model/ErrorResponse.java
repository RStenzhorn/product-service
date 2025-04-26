package de.rjst.ps.api.model;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ErrorResponse {

    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
