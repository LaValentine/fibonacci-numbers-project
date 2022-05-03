package lav.valentine.senderfibonaccinumbers.service.impl;

import lav.valentine.senderfibonaccinumbers.data.FibonacciNumber;
import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FibonacciNumberServiceImpl implements FibonacciNumberService {
    private Mono<FibonacciNumber> currentFibonacciNumbers;
    private final Mono<Integer> amountFibonacciNumber;

    public FibonacciNumberServiceImpl(@Value("${amount-fibonacci-numbers}") Integer amountFibonacciNumber) {
        this.amountFibonacciNumber = Mono.just(amountFibonacciNumber);
        this.currentFibonacciNumbers = Mono.just(new FibonacciNumber());
    }

    @Override
    public Mono<FibonacciNumberDto> nextFibonacciNumber() {
        return currentFibonacciNumbers
                .zipWith(amountFibonacciNumber)
                .flatMap(t2 -> {
                    if(t2.getT2() <= t2.getT1().getIndexOfCurrentFibonacciNumber()) {
                        currentFibonacciNumbers = Mono.just(new FibonacciNumber().nextFibonacciNumber());
                    }
                    t2.getT1().nextFibonacciNumber();
                    return currentFibonacciNumbers;
                })
                .map(fibonacciNumber -> FibonacciNumberDto.builder()
                        .indexOfFibonacciNumber(fibonacciNumber.getIndexOfCurrentFibonacciNumber())
                        .fibonacciNumber(fibonacciNumber.getCurrentFibonacciNumber())
                        .build());
    }

    @Override
    public Mono<Integer> getAmountFibonacciNumbers() {
        return amountFibonacciNumber;
    }
}