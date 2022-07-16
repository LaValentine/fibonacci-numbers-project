package lav.valentine.senderfibonaccinumbers.service;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import reactor.core.publisher.Flux;

public interface FibonacciNumberService {

    Flux<FibonacciNumberDto> fibonacciNumbers();
}