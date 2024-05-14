package player.test.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import player.test.dto.*;
import reactor.core.publisher.Mono;

@Component
public class PlayerHttpEndpoint {

    private final WebClient webClient;

    @Autowired
    public PlayerHttpEndpoint(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CreateUserRequestDto> createUser(CreateUserRequestDto request) {
        String uri = UriComponentsBuilder.fromPath("/player/create/{editor}")
                .buildAndExpand(request.getLogin())
                .toUriString();

        CreateUserRequestDto playerRequest = request.builder()
                .age(request.getAge())
                .gender(request.getGender())
                .login(request.getLogin())
                .password(request.getPassword())
                .role(request.getRole())
                .screenName(request.getScreenName())
                .build();

        return webClient.post()
                .uri(uri)
                .bodyValue(playerRequest)
                .retrieve()
                .bodyToMono(CreateUserRequestDto.class);
    }

    public Mono<Void> deleteUser(DeleteUserRequestDto request) {
        String uri = UriComponentsBuilder.fromPath("/player/delete/{editor}")
                .queryParam("playerId", request.getPlayerId())
                .buildAndExpand(request.getEditor())
                .toUriString();

        return webClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<GetUserResponseDto> getUser(GetUserRequestDto request) {
        String uri = UriComponentsBuilder.fromPath("/player/get")
                .queryParam("playerId", request.getPlayerId())
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GetUserResponseDto.class);
    }

    public Mono<UpdateUserResponseDto> updateUser(UpdateUserRequestDto request) {
        String uri = UriComponentsBuilder.fromPath("/player/update/{editor}/{id}")
                .buildAndExpand(request.getEditor(), request.getId())
                .toUriString();

        return webClient.put()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UpdateUserResponseDto.class);
    }
}
