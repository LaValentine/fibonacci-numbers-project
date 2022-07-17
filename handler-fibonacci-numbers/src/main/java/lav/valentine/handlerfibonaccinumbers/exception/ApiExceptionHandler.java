package lav.valentine.handlerfibonaccinumbers.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<String>> apiExceptionHandler(ApiException ex) {
        return Mono.just(ResponseEntity
                .status(ex.getHttpStatus())
                .body(ex.getMessage()));
    }
}