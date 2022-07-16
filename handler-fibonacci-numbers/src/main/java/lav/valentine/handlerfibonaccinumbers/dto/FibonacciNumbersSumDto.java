package lav.valentine.handlerfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FibonacciNumbersSumDto {

    @JsonProperty("fibonacci-numbers-sum")
    private Long fibonacciNumbersSum;
    @JsonProperty("min-value")
    private Long minValueOfFibonacciNumber;
    @JsonProperty("max-value")
    private Long maxValueOfFibonacciNumber;
}