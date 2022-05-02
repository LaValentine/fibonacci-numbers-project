package lav.valentine.handlerfibonaccinumbers.controller;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class FibonacciNumbersRSocketHandlerController {
    private final RSocketRequester rSocketRequester;

    public FibonacciNumbersRSocketHandlerController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FibonacciNumberDto> requestResponse() {
        return rSocketRequester
                .route("get-fibonacci-numbers")
                .retrieveFlux(FibonacciNumberDto.class);
    }
}
