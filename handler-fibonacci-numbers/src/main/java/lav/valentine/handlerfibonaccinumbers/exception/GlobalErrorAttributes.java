package lav.valentine.handlerfibonaccinumbers.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private final String EXCEPTION_UNKNOWN;

    public GlobalErrorAttributes(@Value("${exception.unknown}") String exception_server) {
        super();
        this.EXCEPTION_UNKNOWN = exception_server;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        return getError(request) instanceof ApiException
            ? setProperties(getError(request).getMessage(), ((ApiException) getError(request)).getHttpStatus().value())
            : setProperties(EXCEPTION_UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private Map<String, Object> setProperties(String message, Integer httpStatus) {
        Map<String, Object> map = new HashMap<>();

        map.put(ErrorProperty.MESSAGE.getProperty(), message);
        map.put(ErrorProperty.STATUS.getProperty(), httpStatus);

        return map;
    }
}