package player.test.webclient.base;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class BaseWebClientEntrypoint {

    protected final WebClient webClient;
}

