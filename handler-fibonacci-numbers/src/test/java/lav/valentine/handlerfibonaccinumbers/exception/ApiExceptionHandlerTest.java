package lav.valentine.handlerfibonaccinumbers.exception;

import lav.valentine.handlerfibonaccinumbers.controller.FibonacciNumbersRSocketHandlerController;
import lav.valentine.handlerfibonaccinumbers.exception.ext.BadParametersException;
import lav.valentine.handlerfibonaccinumbers.exception.ext.ServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@Import(GlobalErrorAttributes.class)
@PropertySource("classpath:application-test.properties")
@WebFluxTest(controllers = FibonacciNumbersRSocketHandlerController.class)
class ApiExceptionHandlerTest {

    @MockBean
    private FibonacciNumbersRSocketHandlerController controller;

    @Autowired
    private WebTestClient webClient;

    private final static String ERROR_MESSAGE = "error-message";

    @Test
    void apiExceptionHandlerServerException() {
        when(controller.getFibonacciNumbers())
                .thenThrow(new ServerException(ERROR_MESSAGE));

        webClient.get().uri("/api/fibonacci-numbers")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class).isEqualTo(ERROR_MESSAGE);
    }

    @Test
    void apiExceptionHandlerRuntimeException() {
        when(controller.getFibonacciNumbers())
                .thenThrow(new RuntimeException());

        webClient.get().uri("/api/fibonacci-numbers")
                .exchange()
                .expectStatus().is5xxServerError();
    }
    @Test
    void apiExceptionHandlerBadParametersException() {
        Long minValue = 100L;
        Long maxValue = 1L;

        when(controller.getFibonacciNumbersSum(any(), any()))
                .thenThrow(new BadParametersException(ERROR_MESSAGE));

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/fibonacci-numbers/sum")
                        .queryParam("max-value", maxValue)
                        .queryParam("min-value", minValue)
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo(ERROR_MESSAGE);
    }
}