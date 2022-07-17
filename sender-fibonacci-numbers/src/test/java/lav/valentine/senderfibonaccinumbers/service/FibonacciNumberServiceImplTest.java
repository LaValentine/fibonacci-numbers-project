package lav.valentine.senderfibonaccinumbers.service;

import lav.valentine.senderfibonaccinumbers.dto.FibonacciNumberDto;
import lav.valentine.senderfibonaccinumbers.service.impl.FibonacciNumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
class FibonacciNumberServiceImplTest {

    @Autowired
    private FibonacciNumberServiceImpl fibonacciNumberService;

    @Test
    void fibonacciNumbers() {
        List<FibonacciNumberDto> fibonacciNumbers = List.of(
                new FibonacciNumberDto(1L, 1),
                new FibonacciNumberDto(1L, 2),
                new FibonacciNumberDto(2L, 3),
                new FibonacciNumberDto(3L, 4),
                new FibonacciNumberDto(5L, 5));

        StepVerifier.FirstStep<FibonacciNumberDto> firstStep =
                StepVerifier.create(fibonacciNumberService.fibonacciNumbers());
        fibonacciNumbers.forEach(firstStep::expectNext);
        firstStep.verifyComplete();
    }
}