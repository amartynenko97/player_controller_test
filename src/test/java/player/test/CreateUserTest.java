package player.test;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testng.annotations.Test;
import player.test.base.BaseContextTests;
import player.test.dto.CreateUserRequestDto;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CreateUserTest extends BaseContextTests {

    @Test
    public void createUserPositiveTest() {
        var request = CreateUserRequestDto.builder()
                .age("25")
                .gender("male")
                .login("testuser")
                .password("password123")
                .role("user")
                .screenName("Test User")
                .build();

        var responseMono = playerHttpEndpoint.createUser(request);

        CreateUserRequestDto response = responseMono.block();

        assertThat(response).isNotNull();
        assertThat(response.getLogin()).isEqualTo("testuser");
    }

    @Test
    public void createUserNegativeTest() {
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .age("15")
                .gender("male")
                .login("testuser")
                .password("password123")
                .role("user")
                .screenName("Test User")
                .build();

        assertThatThrownBy(() -> {
            Mono<CreateUserRequestDto> responseMono = playerHttpEndpoint.createUser(request);
            responseMono.block();
        }).isInstanceOf(WebClientResponseException.class);
    }
}
