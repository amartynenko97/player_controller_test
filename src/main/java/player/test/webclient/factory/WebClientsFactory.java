package player.test.webclient.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class WebClientsFactory {

    private final ObjectMapper objectMapper;
    private final HashMap<String, WebClient> clients = new HashMap<>();

    public WebClient createClient(String baseUrl) {
        return clients.computeIfAbsent(baseUrl, __ -> createClientBuilder(baseUrl).build());
    }

    public WebClient.Builder createClientBuilder(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(codecs -> {
                    codecs.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    codecs.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                    codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
                })
                .clientConnector(new JettyClientHttpConnector());
    }
}

