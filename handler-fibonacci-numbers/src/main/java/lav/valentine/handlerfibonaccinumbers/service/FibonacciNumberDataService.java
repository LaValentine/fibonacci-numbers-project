package lav.valentine.handlerfibonaccinumbers.service;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FibonacciNumberDataService {

    Flux<FibonacciNumberDto> fibonacciNumbersBetweenFilter(Flux<FibonacciNumberDto> fibonacciNumbers, Long minValue, Long maxValue);

    Mono<FibonacciNumbersSumDto> fibonacciNumbersBetweenSum(Flux<FibonacciNumberDto> fibonacciNumbers, Long minValue, Long maxValue);
}
