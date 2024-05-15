package player.test.endpoints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import player.test.asserter.WebClientException;
import player.test.dto.*;
import player.test.webclient.base.BaseWebClientEntrypoint;
import player.test.webclient.factory.WebClientsFactory;
import player.test.webclient.filter.WebClientFilterFunctions;
import player.test.webclient.mapper.ObjectToQueryMapFunction;

import java.util.List;

@Component
public class PlayerHttpEndpoint extends BaseWebClientEntrypoint {


    public PlayerHttpEndpoint(@Value("${app.url}") String url, WebClientsFactory factory) {
        super(
                factory.createClientBuilder(url)
                        .filter(WebClientFilterFunctions.plusFilterFunction)
                        .build()
        );
    }

    public CreateUserResponseDto createUser(String editor, CreateUserRequestDto request) {
        String uri = UriComponentsBuilder.fromPath("/player/create/{editor}")
                .queryParams(ObjectToQueryMapFunction.apply(request))
                .buildAndExpand(editor)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(CreateUserResponseDto.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    throw new WebClientException(ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .block();
    }

    public void deleteUser(DeleteUserRequestDto request, String editor) {
        String uri = UriComponentsBuilder.fromPath("/player/delete/{editor}")
                .buildAndExpand(editor)
                .toUriString();

        webClient.method(HttpMethod.DELETE)
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    throw new WebClientException(ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .block();
    }

    public GetUserResponseDto getUser(GetUserRequestDto request) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/player/get")
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GetUserResponseDto.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    throw new WebClientException(ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .block();
    }

    public List<PlayerDto> getAllPlayers() {
        return webClient.get()
                .uri("/player/get/all")
                .retrieve()
                .bodyToFlux(PlayerDto.class)
                .collectList()
                .block();
    }

    public UpdateUserResponseDto updateUser(UpdateUserRequestDto request, Integer userId, String editor) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/player/update/{editor}/{id}")
                        .build(editor, userId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UpdateUserResponseDto.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    throw new WebClientException(ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .block();
    }
}

