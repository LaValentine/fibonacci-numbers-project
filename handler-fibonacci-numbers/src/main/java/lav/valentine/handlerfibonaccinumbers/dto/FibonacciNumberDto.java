package lav.valentine.handlerfibonaccinumbers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FibonacciNumberDto {

    @JsonProperty("fibonacci-number")
    private Long fibonacciNumber;
    @JsonProperty("index")
    private Integer indexOfFibonacciNumber;
}