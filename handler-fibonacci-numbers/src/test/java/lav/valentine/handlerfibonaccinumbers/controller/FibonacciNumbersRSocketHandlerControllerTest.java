package lav.valentine.handlerfibonaccinumbers.controller;

import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.handlerfibonaccinumbers.dto.FibonacciNumbersSumDto;
import lav.valentine.handlerfibonaccinumbers.exception.GlobalErrorAttributes;
import lav.valentine.handlerfibonaccinumbers.service.FibonacciNumberDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = FibonacciNumbersRSocketHandlerController.class)
@DirtiesContext
@Import(GlobalErrorAttributes.class)
class FibonacciNumbersRSocketHandlerControllerTest {

    @MockBean
    private FibonacciNumberDataService fibonacciNumberDataService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getFibonacciNumbersSum() {
        Long minValue = 1L;
        Long maxValue = 10L;
        Long sum = 20L;

        Mono<FibonacciNumbersSumDto> fibonacciNumbersSum =
                Mono.just(new FibonacciNumbersSumDto(sum, minValue, maxValue));

        Mockito.when(fibonacciNumberDataService.fibonacciNumbersBetweenSum(minValue, maxValue))
                .thenReturn(fibonacciNumbersSum);

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fibonacci-numbers/get-fibonacci-numbers-sum")
                    .queryParam("max-value", maxValue)
                    .queryParam("min-value", minValue)
                    .build())
                .exchange()
                    .expectStatus()
                        .isOk()
                    .expectBody()
                        .jsonPath("fibonacci-numbers-sum").isEqualTo(sum)
                        .jsonPath("min-value").isEqualTo(minValue)
                        .jsonPath("max-value").isEqualTo(maxValue);
    }

    @Test
    void getFibonacciNumbers() {
        List<FibonacciNumberDto> fibonacciNumbers = List.of(
                new FibonacciNumberDto(1L, 1),
                new FibonacciNumberDto(1L, 2),
                new FibonacciNumberDto(2L, 3),
                new FibonacciNumberDto(3L, 4),
                new FibonacciNumberDto(5L, 5));
        Flux<FibonacciNumberDto> fibonacciNumbersFlux = Flux.fromIterable(fibonacciNumbers);

        Mockito.when(fibonacciNumberDataService.getFibonacciNumbers()).thenReturn(fibonacciNumbersFlux);

        webClient.get().uri("/fibonacci-numbers/get-fibonacci-numbers")
                .exchange()
                    .expectStatus()
                        .isOk()
                    .expectBodyList(FibonacciNumberDto.class)
                        .contains(fibonacciNumbers.get(0),
                                fibonacciNumbers.get(1),
                                fibonacciNumbers.get(2),
                                fibonacciNumbers.get(3),
                                fibonacciNumbers.get(4));
    }
}