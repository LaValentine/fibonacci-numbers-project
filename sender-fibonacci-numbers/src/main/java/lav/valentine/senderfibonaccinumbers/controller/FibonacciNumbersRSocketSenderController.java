package lav.valentine.senderfibonaccinumbers.controller;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class FibonacciNumbersRSocketSenderController {
    private final FibonacciNumberService fibonacciNumberService;
    private final Integer intervalOfSending;

    public FibonacciNumbersRSocketSenderController(FibonacciNumberService fibonacciNumberService,
                                                   @Value("${interval-of-sending}") Integer intervalOfSending) {
        this.fibonacciNumberService = fibonacciNumberService;
        this.intervalOfSending = intervalOfSending;
    }

    @MessageMapping("get-fibonacci-numbers")
    public Flux<FibonacciNumberDto> sendFibonacciNumbers() {
        return Flux.interval(Duration.ofMillis(intervalOfSending))
                .flatMap(t -> fibonacciNumberService.nextFibonacciNumber());
    }

    @MessageMapping("get-some-fibonacci-numbers")
    public Flux<FibonacciNumberDto> sendSomeFibonacciNumbers() {
        return Flux.interval(Duration.ofMillis(intervalOfSending))
                .flatMap(t -> fibonacciNumberService.nextFibonacciNumber())
                .take(fibonacciNumberService.getAmountFibonacciNumbers().block());
    }
}