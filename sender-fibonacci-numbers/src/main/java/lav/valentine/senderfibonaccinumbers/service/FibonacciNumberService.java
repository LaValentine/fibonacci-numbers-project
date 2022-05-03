package lav.valentine.senderfibonaccinumbers.service;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import reactor.core.publisher.Mono;

public interface FibonacciNumberService {

    Mono<FibonacciNumberDto> nextFibonacciNumber();

    Mono<Integer> getAmountFibonacciNumbers();
}
