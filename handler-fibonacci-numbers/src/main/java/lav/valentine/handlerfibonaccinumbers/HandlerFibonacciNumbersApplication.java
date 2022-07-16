package lav.valentine.handlerfibonaccinumbers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HandlerFibonacciNumbersApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandlerFibonacciNumbersApplication.class, args);
    }

}
