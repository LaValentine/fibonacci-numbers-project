package lav.valentine.handlerfibonaccinumbers.service.impl;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.exception.ext.BadParametersException;
import lav.valentine.handlerfibonaccinumbers.exception.ext.ServerException;
import lav.valentine.handlerfibonaccinumbers.service.FibonacciNumberDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FibonacciNumberDataServiceImpl implements FibonacciNumberDataService {

    private final RSocketRequester rSocketRequester;
    private final String EXCEPTION_MIN_BIGGER_MAX;
    private final String EXCEPTION_SERVER;

    public FibonacciNumberDataServiceImpl(RSocketRequester rSocketRequester,
                                          @Value("${exception.user.min-bigger-max}") String exceptionMinBiggerMax,
                                          @Value("${exception.server}") String exceptionServer) {
        this.rSocketRequester = rSocketRequester;
        this.EXCEPTION_MIN_BIGGER_MAX = exceptionMinBiggerMax;
        this.EXCEPTION_SERVER = exceptionServer;
    }

    private Flux<Long> fibonacciNumbersBetweenFilter(Flux<Long> fibonacciNumbers, Long minValue, Long maxValue) {
        return fibonacciNumbers.filter(fibonacciNumber -> fibonacciNumber < maxValue && fibonacciNumber > minValue);
    }

    @Override
    public Mono<FibonacciNumbersSumDto> fibonacciNumbersBetweenSum(Long minValue, Long maxValue) {
        if (minValue >= maxValue) {
            throw new BadParametersException(EXCEPTION_MIN_BIGGER_MAX);
        }
        return fibonacciNumbersBetweenFilter(
                getFibonacciNumbers().map(FibonacciNumberDto::getFibonacciNumber), minValue, maxValue)
                .reduce(Long::sum)
                .map(sum -> FibonacciNumbersSumDto.builder()
                            .fibonacciNumbersSum(sum)
                            .maxValueOfFibonacciNumber(maxValue)
                            .minValueOfFibonacciNumber(minValue)
                            .build());
    }

    public Flux<FibonacciNumberDto> getFibonacciNumbers() {
        return rSocketRequester
                    .route("get-fibonacci-numbers-from-stream")
                    .retrieveFlux(FibonacciNumberDto.class)
                    .onErrorResume(err -> {
                        throw new ServerException(EXCEPTION_SERVER);
                    });
    }
}