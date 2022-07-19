package lav.valentine.handlerfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FibonacciNumbersSumDto {

    @JsonProperty("fibonacci-numbers-sum")
    private Long fibonacciNumbersSum = 0L;
    @JsonProperty("min-value")
    private Long minValueOfFibonacciNumber;
    @JsonProperty("max-value")
    private Long maxValueOfFibonacciNumber;
}