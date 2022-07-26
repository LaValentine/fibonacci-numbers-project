package lav.valentine.handlerfibonaccinumbers.service;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FibonacciNumberDataService {

    Mono<FibonacciNumbersSumDto> fibonacciNumbersBetweenSum(Long minValue, Long maxValue);

    Flux<FibonacciNumberDto> getFibonacciNumbers();
}