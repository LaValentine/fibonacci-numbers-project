package lav.valentine.senderfibonaccinumbers.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FibonacciNumber {
    private Long currentFibonacciNumber = 0L;
    private Integer indexOfCurrentFibonacciNumber = 0;

    private Long nextFibonacciNumber = 1L;

    public FibonacciNumber nextFibonacciNumber() {
        Long temp = currentFibonacciNumber;
        currentFibonacciNumber = nextFibonacciNumber;
        nextFibonacciNumber = temp + currentFibonacciNumber;
        indexOfCurrentFibonacciNumber++;

        return this;
    }
}