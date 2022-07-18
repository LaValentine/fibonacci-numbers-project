package lav.valentine.handlerfibonaccinumbers.exception.ext;

import lav.valentine.handlerfibonaccinumbers.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ServerException extends ApiException {

    public ServerException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}