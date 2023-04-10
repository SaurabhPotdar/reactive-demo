package com.tce.exception;

import com.tce.commons.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({WebClientResponseException.ServiceUnavailable.class})
    public ResponseEntity<ErrorResponse> webClientResponseEX(WebClientResponseException.ServiceUnavailable ex) {
        log.error("WebClientRequestException ERROR >>>", ex);
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(ex.getStatusCode().value(), ex.getMessage()));
    }

}
