package lav.valentine.senderfibonaccinumbers.service.impl;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Service
public class FibonacciNumberServiceImpl implements FibonacciNumberService {

    private final Integer amountFibonacciNumber;
    private final Integer intervalOfSending;

    private long previousNum = 0;

    public FibonacciNumberServiceImpl(@Value("${amount-fibonacci-numbers}") Integer amountFibonacciNumber,
                                      @Value("${interval-of-sending}") Integer intervalOfSending) {
        this.amountFibonacciNumber = amountFibonacciNumber;
        this.intervalOfSending = intervalOfSending;
    }

    private Flux<FibonacciNumberDto> generateFibonacciNumbers() {
        log.info("Generating fibonacci numbers");
        return Flux.generate(
                () -> new FibonacciNumberDto(1L, 1),
                (state, sink) -> {
                    sink.next(state);
                    long temp = state.getFibonacciNumber();
                    FibonacciNumberDto fibonacciNumberDto = new FibonacciNumberDto(
                            state.getFibonacciNumber() + previousNum,
                            state.getIndexOfFibonacciNumber() + 1);
                    previousNum = temp;
                    return fibonacciNumberDto;
                });
    }

    public Flux<FibonacciNumberDto> fibonacciNumbers() {
        previousNum = 0;
        return generateFibonacciNumbers()
                .take(amountFibonacciNumber)
                .delayElements(Duration.ofMillis(intervalOfSending));
    }
}