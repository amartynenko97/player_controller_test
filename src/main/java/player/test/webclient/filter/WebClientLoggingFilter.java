package player.test.webclient.filter;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class WebClientLoggingFilter {

    public static ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            System.out.println("Request: " + request.method() + " " + request.url());
            return next.exchange(request);
        };
    }

    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(
                response -> response.bodyToMono(String.class)
                        .flatMap(body -> {
                            System.out.println("Response Status: " + response.statusCode());
                            System.out.println("Response Body: " + body);
                            return Mono.just(ClientResponse.from(response).body(body).build());
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            System.out.println("Response Status: " + response.statusCode());
                            return Mono.just(response);
                        })));
    }
}
