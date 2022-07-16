package lav.valentine.handlerfibonaccinumbers.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    protected HttpStatus httpStatus;

    public ApiException(String message) {
        super(message);
    }
}