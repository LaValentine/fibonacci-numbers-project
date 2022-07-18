package lav.valentine.handlerfibonaccinumbers.controller;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.service.FibonacciNumberDataService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fibonacci-numbers")
public class FibonacciNumbersRSocketHandlerController {

    private final FibonacciNumberDataService fibonacciNumberDataService;

    public FibonacciNumbersRSocketHandlerController(FibonacciNumberDataService fibonacciNumberDataService) {
        this.fibonacciNumberDataService = fibonacciNumberDataService;
    }

    @Cacheable(value = "fibonacci-numbers-sum")
    @GetMapping(value = "/get-fibonacci-numbers-sum")
    public Mono<FibonacciNumbersSumDto> getFibonacciNumbersSum(@RequestParam("max-value") Long maxValue,
                                                               @RequestParam("min-value") Long minValue) {
        return fibonacciNumberDataService.fibonacciNumbersBetweenSum(minValue, maxValue).cache();
    }

    @Cacheable(value = "fibonacci-numbers")
    @GetMapping(value = "/get-fibonacci-numbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FibonacciNumberDto> getFibonacciNumbers() {
        return fibonacciNumberDataService.getFibonacciNumbers().cache();
    }
}