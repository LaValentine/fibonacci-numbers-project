package lav.valentine.handlerfibonaccinumbers.service.impl;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.exception.ext.BadParametersException;
import lav.valentine.handlerfibonaccinumbers.exception.ext.ServerException;
import lav.valentine.handlerfibonaccinumbers.service.FibonacciNumberDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FibonacciNumberDataServiceImpl implements FibonacciNumberDataService {

    private final RSocketRequester rSocketRequester;
    private final String EXCEPTION_MIN_GREATER_MAX;
    private final String EXCEPTION_SERVER;

    public FibonacciNumberDataServiceImpl(RSocketRequester rSocketRequester,
                                          @Value("${exception.user.min-greater-max}") String exceptionMinGreaterMax,
                                          @Value("${exception.rsocket-server}") String exceptionServer) {
        this.rSocketRequester = rSocketRequester;
        this.EXCEPTION_MIN_GREATER_MAX = exceptionMinGreaterMax;
        this.EXCEPTION_SERVER = exceptionServer;
    }

    private Flux<Long> fibonacciNumbersBetweenFilter(Flux<Long> fibonacciNumbers, Long minValue, Long maxValue) {
        log.info("Selecting of Fibonacci numbers from {} to {}", minValue, maxValue);
        return fibonacciNumbers.filter(fibonacciNumber -> fibonacciNumber <= maxValue && fibonacciNumber >= minValue);
    }

    @Override
    public Mono<FibonacciNumbersSumDto> fibonacciNumbersBetweenSum(Long minValue, Long maxValue) {
        if (minValue > maxValue) {
            log.info(EXCEPTION_MIN_GREATER_MAX);
            return Mono.error(new BadParametersException(EXCEPTION_MIN_GREATER_MAX));
        }
        return fibonacciNumbersBetweenFilter(
                getFibonacciNumbers().map(FibonacciNumberDto::getFibonacciNumber), minValue, maxValue)
                .reduce(Long::sum)
                .map(sum -> new FibonacciNumbersSumDto(sum, minValue, maxValue))
                .defaultIfEmpty(new FibonacciNumbersSumDto(0L, minValue, maxValue)).log();
    }

    @Override
    public Flux<FibonacciNumberDto> getFibonacciNumbers() {
        return rSocketRequester
                .route("get-fibonacci-numbers")
                .retrieveFlux(FibonacciNumberDto.class)
                .onErrorResume(err -> {
                    log.error("{}, {}", EXCEPTION_SERVER, err.getMessage());
                    return Flux.error(new ServerException(EXCEPTION_SERVER));
                });
    }
}