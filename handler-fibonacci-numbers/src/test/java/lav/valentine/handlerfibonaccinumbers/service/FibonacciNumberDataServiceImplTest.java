package lav.valentine.handlerfibonaccinumbers.service;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.exception.ext.BadParametersException;
import lav.valentine.handlerfibonaccinumbers.exception.ext.ServerException;
import lav.valentine.handlerfibonaccinumbers.service.impl.FibonacciNumberDataServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
class FibonacciNumberDataServiceImplTest {
    @MockBean
    private RSocketRequester rSocketRequester;
    @MockBean
    private RSocketRequester.RequestSpec requestSpec;

    @Autowired
    private FibonacciNumberDataServiceImpl fibonacciNumberDataService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fibonacciNumbersBetweenSum() {
        List<FibonacciNumberDto> fibonacciNumbers = List.of(
                new FibonacciNumberDto(1L, 1),
                new FibonacciNumberDto(1L, 2),
                new FibonacciNumberDto(2L, 3),
                new FibonacciNumberDto(3L, 4),
                new FibonacciNumberDto(5L, 5));
        Flux<FibonacciNumberDto> fibonacciNumbersFlux = Flux.fromIterable(fibonacciNumbers);

        Long maxValue = 2L;
        Long minValue = 0L;

        when(rSocketRequester.route("get-fibonacci-numbers")).thenReturn(requestSpec);
        when(requestSpec.retrieveFlux(FibonacciNumberDto.class))
                .thenReturn(fibonacciNumbersFlux);

        StepVerifier.create(fibonacciNumberDataService.fibonacciNumbersBetweenSum(minValue, maxValue))
                .expectNextMatches(t -> t.getFibonacciNumbersSum() == 4).verifyComplete();
    }

    @Test
    void fibonacciNumbersBetweenSumMinBiggerThenMax() {
        Long maxValue = 0L;
        Long minValue = 2L;

        StepVerifier.create(fibonacciNumberDataService.fibonacciNumbersBetweenSum(minValue, maxValue))
                .expectError(BadParametersException.class).verify();
    }

    @Test
    void getFibonacciNumbers() {
        when(rSocketRequester.route("get-fibonacci-numbers")).thenReturn(requestSpec);
        when(requestSpec.retrieveFlux(FibonacciNumberDto.class))
                .thenReturn(Flux.error(new Exception("")));
        StepVerifier.create(fibonacciNumberDataService.getFibonacciNumbers())
                .expectError(ServerException.class).verify();
    }
}