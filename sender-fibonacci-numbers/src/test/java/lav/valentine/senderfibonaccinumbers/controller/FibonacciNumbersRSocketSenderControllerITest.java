package lav.valentine.senderfibonaccinumbers.controller;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.FibonacciNumberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
class FibonacciNumbersRSocketSenderControllerITest {

    @MockBean
    private FibonacciNumberService fibonacciNumberService;

    private RSocketRequester rSocketRequester;

    @BeforeEach
    public void setupOnce(@Autowired RSocketRequester.Builder builder,
                          @LocalRSocketServerPort Integer port) {
        rSocketRequester = builder.tcp("localhost", port);
    }

    @AfterEach
    public void tearDown() {
        rSocketRequester.dispose();
    }

    @Test
    void sendFibonacciNumbers() {
        List<FibonacciNumberDto> fibonacciNumbers = List.of(
                new FibonacciNumberDto(1L, 1),
                new FibonacciNumberDto(1L, 2),
                new FibonacciNumberDto(2L, 3),
                new FibonacciNumberDto(3L, 4),
                new FibonacciNumberDto(5L, 5));
        Flux<FibonacciNumberDto> fibonacciNumbersFlux = Flux.fromIterable(fibonacciNumbers);

        when(fibonacciNumberService.fibonacciNumbers()).thenReturn(fibonacciNumbersFlux);

        Flux<FibonacciNumberDto> f = rSocketRequester
                .route("get-fibonacci-numbers")
                .retrieveFlux(FibonacciNumberDto.class);

        StepVerifier.FirstStep<FibonacciNumberDto> firstStep = StepVerifier.create(f);
        fibonacciNumbers.forEach(firstStep::expectNext);
        firstStep.verifyComplete();
    }
}