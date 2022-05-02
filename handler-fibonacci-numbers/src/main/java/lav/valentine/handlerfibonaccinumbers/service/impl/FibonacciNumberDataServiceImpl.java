package lav.valentine.handlerfibonaccinumbers.service.impl;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.service.FibonacciNumberDataService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FibonacciNumberDataServiceImpl implements FibonacciNumberDataService {

    @Override
    public Flux<FibonacciNumberDto> fibonacciNumbersBetweenFilter(Flux<FibonacciNumberDto> fibonacciNumbers,
                                                                  Long minValue,
                                                                  Long maxValue) {
        return fibonacciNumbers.filter(
                fibonacciNumberDto ->
                        fibonacciNumberDto.getFibonacciNumber() < maxValue
                        && fibonacciNumberDto.getFibonacciNumber() > minValue);
    }

    @Override
    public Mono<FibonacciNumbersSumDto> fibonacciNumbersBetweenSum(Flux<FibonacciNumberDto> fibonacciNumbers,
                                                                   Long minValue,
                                                                   Long maxValue) {
        return fibonacciNumbersBetweenFilter(fibonacciNumbers, minValue, maxValue)
                .map(FibonacciNumberDto::getFibonacciNumber)
                .collectList()
                .map(t -> t.stream().mapToLong(a -> a).sum())
                .map(sum -> {
                    System.out.println(sum);
                    return FibonacciNumbersSumDto.builder()
                            .fibonacciNumbersSum(sum)
                            .maxValueOfFibonacciNumber(maxValue)
                            .minValueOfFibonacciNumber(minValue)
                            .build();
                });
    }
}
