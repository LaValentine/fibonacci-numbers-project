package lav.valentine.handlerfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FibonacciNumberDto {

    @JsonProperty("fibonacci-number")
    private Long fibonacciNumber;
    @JsonProperty("index-of-fibonacci-number")
    private Integer indexOfFibonacciNumber;
}