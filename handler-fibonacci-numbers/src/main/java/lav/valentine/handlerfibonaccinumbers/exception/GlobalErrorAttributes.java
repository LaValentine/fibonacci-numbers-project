package lav.valentine.handlerfibonaccinumbers.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = new HashMap<>();

        if (getError(request) instanceof ApiException) {
            ApiException ex = (ApiException) getError(request);
            map.put("message", ex.getMessage());
            map.put("status", ex.getHttpStatus().value());

        }
        else {
            map.put("message", "Unknown exception");
            map.put("status", 500);
        }
        return map;
    }

}
