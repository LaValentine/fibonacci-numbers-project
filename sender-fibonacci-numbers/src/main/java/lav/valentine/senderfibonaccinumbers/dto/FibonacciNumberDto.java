package lav.valentine.senderfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FibonacciNumberDto {

    @JsonProperty("fibonacci-number")
    private Long fibonacciNumber;
    @JsonProperty("index")
    private Integer indexOfFibonacciNumber;
}