package player.test.webclient.filter;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.util.UriComponentsBuilder;

public class WebClientFilterFunctions {

    public static final ExchangeFilterFunction plusFilterFunction = (clientRequest, nextFilter) -> {
        if (clientRequest.url().getRawQuery() == null) {
            return nextFilter.exchange(clientRequest);
        }

        var escapedUri = UriComponentsBuilder.fromUri(clientRequest.url())
                .replaceQuery(clientRequest.url().getRawQuery().replace("+", "%2B"))
                .build(true)
                .toUri();

        return nextFilter.exchange(
                ClientRequest.from(clientRequest)
                        .url(escapedUri)
                        .build()
        );
    };
}
