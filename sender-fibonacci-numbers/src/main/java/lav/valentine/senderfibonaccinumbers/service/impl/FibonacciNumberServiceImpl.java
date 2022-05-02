package lav.valentine.senderfibonaccinumbers.service.impl;

import lav.valentine.senderfibonaccinumbers.data.FibonacciNumber;
import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FibonacciNumberServiceImpl implements FibonacciNumberService {
    private static final Mono<Integer> AMOUNT_FIBONACCI_NUMBER = Mono.just(50);
    private static Mono<FibonacciNumber> CURRENT_FIBONACCI_NUMBER = Mono.just(new FibonacciNumber());

    @Override
    public Mono<FibonacciNumberDto> nextFibonacciNumber() {
        return CURRENT_FIBONACCI_NUMBER
                .zipWith(AMOUNT_FIBONACCI_NUMBER)
                .flatMap(t2 -> {
                    if(t2.getT1().getIndexOfCurrentFibonacciNumber() < t2.getT2()) {
                        t2.getT1().nextFibonacciNumber();
                    }
                    else {
                        CURRENT_FIBONACCI_NUMBER = Mono.just(new FibonacciNumber());
                    }
                    return CURRENT_FIBONACCI_NUMBER;
                })
                .map(fibonacciNumber -> FibonacciNumberDto.builder()
                        .indexOfFibonacciNumber(fibonacciNumber.getIndexOfCurrentFibonacciNumber())
                        .fibonacciNumber(fibonacciNumber.getCurrentFibonacciNumber())
                        .build());

    }
}
