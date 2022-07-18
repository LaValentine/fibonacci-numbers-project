package lav.valentine.senderfibonaccinumbers.controller;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class FibonacciNumbersRSocketSenderController {
    private final FibonacciNumberService fibonacciNumberService;

    public FibonacciNumbersRSocketSenderController(FibonacciNumberService fibonacciNumberService) {
        this.fibonacciNumberService = fibonacciNumberService;
    }

    @MessageMapping("get-fibonacci-numbers")
    public Flux<FibonacciNumberDto> sendFibonacciNumbersFromStream() {
        return fibonacciNumberService.fibonacciNumbers();
    }
}