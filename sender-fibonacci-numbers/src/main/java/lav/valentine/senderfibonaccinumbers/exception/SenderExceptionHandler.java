package lav.valentine.senderfibonaccinumbers.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class SenderExceptionHandler {

    @MessageExceptionHandler
    public Mono<SenderException> exceptionHandler(SenderException exception) {
        return Mono.error(exception);
    }
}
