package lav.valentine.handlerfibonaccinumbers.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    public Mono<ResponseEntity<String>> apiExceptionHandler(ApiException ex) {
        return Mono.just(ResponseEntity
                .status(ex.getHttpStatus())
                .body(ex.getMessage()));
    }
}