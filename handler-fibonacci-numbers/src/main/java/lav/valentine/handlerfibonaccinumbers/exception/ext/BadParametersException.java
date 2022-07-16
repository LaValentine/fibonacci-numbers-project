package lav.valentine.handlerfibonaccinumbers.exception.ext;

import lav.valentine.handlerfibonaccinumbers.exception.ApiException;
import org.springframework.http.HttpStatus;

public class BadParametersException extends ApiException {

    public BadParametersException(String message) {
        super(message);
        super.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
