package lav.valentine.senderfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FibonacciNumberDto {

    @JsonProperty("fibonacci-number")
    private Integer fibonacciNumber;
    @JsonProperty("index-of-fibonacci-number")
    private Integer indexOfFibonacciNumber;
}