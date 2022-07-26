package lav.valentine.handlerfibonaccinumbers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

@Configuration
public class RSocketClientConfig {

    @Bean
    public RSocketRequester rSocketRequester(@Autowired RSocketRequester.Builder builder,
                                             @Value("${rsocket.requester.tcp.host}") String host,
                                             @Value("${rsocket.requester.tcp.port}") Integer port) {
        return builder.tcp(host, port);
    }
}