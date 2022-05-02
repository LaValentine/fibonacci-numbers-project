package lav.valentine.handlerfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FibonacciNumbersSumDto {

    @JsonProperty("fibonacci-numbers-sum")
    private Long fibonacciNumbersSum;
    @JsonProperty("min-value-of-fibonacci-numbers")
    private Long minValueOfFibonacciNumber;
    @JsonProperty("max-value-of-fibonacci-numbers")
    private Long maxValueOfFibonacciNumber;
}
