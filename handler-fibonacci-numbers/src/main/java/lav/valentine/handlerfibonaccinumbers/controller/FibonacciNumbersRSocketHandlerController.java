package lav.valentine.handlerfibonaccinumbers.controller;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.service.FibonacciNumberDataService;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fibonacci-numbers")
public class FibonacciNumbersRSocketHandlerController {
    private final RSocketRequester rSocketRequester;
    private final FibonacciNumberDataService fibonacciNumberDataService;

    public FibonacciNumbersRSocketHandlerController(RSocketRequester rSocketRequester,
                                                    FibonacciNumberDataService fibonacciNumberDataService) {
        this.rSocketRequester = rSocketRequester;
        this.fibonacciNumberDataService = fibonacciNumberDataService;
    }

    @GetMapping(value = "/get-fibonacci-numbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FibonacciNumberDto> getFibonacciNumbers() {
        return rSocketRequester
                .route("get-fibonacci-numbers")
                .retrieveFlux(FibonacciNumberDto.class);
    }

    @GetMapping(value = "/get-some-fibonacci-numbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FibonacciNumberDto> getSomeFibonacciNumbers() {
        return rSocketRequester
                .route("get-some-fibonacci-numbers")
                .retrieveFlux(FibonacciNumberDto.class);
    }

    @GetMapping(value = "/get-sum-of-some-fibonacci-numbers")
    public Mono<FibonacciNumbersSumDto> getFibonacciNumbersSum(
            @RequestParam Long maxValue,
            @RequestParam Long minValue) {
        return fibonacciNumberDataService.fibonacciNumbersBetweenSum(
                rSocketRequester
                        .route("get-some-fibonacci-numbers")
                        .retrieveFlux(FibonacciNumberDto.class), minValue, maxValue);
    }
}
