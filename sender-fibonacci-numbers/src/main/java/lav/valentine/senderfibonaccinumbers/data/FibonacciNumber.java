package lav.valentine.senderfibonaccinumbers.data;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class FibonacciNumber {
    private Integer currentFibonacciNumber = 0;
    private Integer indexOfCurrentFibonacciNumber = 0;

    private Integer nextFibonacciNumber = 1;

    public FibonacciNumber nextFibonacciNumber() {
        Integer temp = currentFibonacciNumber;
        currentFibonacciNumber = nextFibonacciNumber;
        nextFibonacciNumber = temp + currentFibonacciNumber;
        indexOfCurrentFibonacciNumber++;

        return this;
    }
}
