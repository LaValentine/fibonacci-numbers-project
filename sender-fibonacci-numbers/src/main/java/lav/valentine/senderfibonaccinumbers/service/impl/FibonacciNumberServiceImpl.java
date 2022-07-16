package lav.valentine.senderfibonaccinumbers.service.impl;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.exception.SenderException;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class FibonacciNumberServiceImpl implements FibonacciNumberService {

    private final Integer amountFibonacciNumber;
    private final Integer intervalOfSending;
    private final String EXCEPTION_PROPERTY;

    public FibonacciNumberServiceImpl(@Value("${amount-fibonacci-numbers}") Integer amountFibonacciNumber,
                                      @Value("${interval-of-sending}") Integer intervalOfSending,
                                      @Value("${exception.property}") String exceptionProperty) {
        this.amountFibonacciNumber = amountFibonacciNumber;
        this.intervalOfSending = intervalOfSending;
        this.EXCEPTION_PROPERTY = exceptionProperty;
    }

    private long previousNum = 0;

    private Flux<FibonacciNumberDto> generateFibonacciNumbers() {
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
        try {
            previousNum = 0;
            return generateFibonacciNumbers()
                    .take(amountFibonacciNumber)
                    .delayElements(Duration.ofMillis(intervalOfSending));
        }
        catch (IllegalArgumentException ex) {
            throw new SenderException(EXCEPTION_PROPERTY);
        }
    }
}