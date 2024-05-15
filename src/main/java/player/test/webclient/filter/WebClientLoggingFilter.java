package player.test.webclient.filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class WebClientLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logRequestDetails(request);
            return Mono.just(request);
        });
    }

    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            return response.bodyToMono(String.class)
                    .flatMap(body -> {
                        logResponseDetails(response, body);
                        ClientResponse newResponse = ClientResponse.create(response.statusCode())
                                .headers(headers -> headers.addAll(response.headers().asHttpHeaders()))
                                .body(body)
                                .build();
                        return Mono.just(newResponse);
                    });
        });
    }

    private static void logRequestDetails(ClientRequest request) {
        logger.info("Request: {} {}", request.method(), request.url());
        request.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
    }

    private static void logResponseDetails(ClientResponse response, String body) {
        logger.info("Response Status: {}", response.statusCode());
        response.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
        logger.info("Response Body: {}", body);
    }
}
