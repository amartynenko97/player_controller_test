package player.test;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import player.test.base.BaseContextTests;
import player.test.dto.CreateUserRequestDto;
import player.test.dto.DeleteUserRequestDto;
import player.test.dto.GetUserRequestDto;
import player.test.statics.UserRole;
import player.test.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static player.test.asserter.WebClientErrorAsserter.assertHttpFails;

public class GetUserTest extends BaseContextTests {

    @Test
    public void positive() {
        var userId = createUserAndGetId(UserRole.ADMIN.getRole());

        var request = GetUserRequestDto.builder()
                .playerId(userId)
                .build();

        var response = playerHttpEndpoint.getUser(request);

        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getRole()).isEqualTo(UserRole.ADMIN.getRole());
    }


    @Test
    public void negativeGetNotExistentUser() {
        var userId = createUserAndGetId(UserRole.USER.getRole());

        var request = GetUserRequestDto.builder()
                .playerId(userId)
                .build();

        playerHttpEndpoint.deleteUser(
                DeleteUserRequestDto.builder()
                        .playerId(userId)
                        .build(),
                UserRole.ADMIN.getRole()
        );
        assertHttpFails(
                () -> playerHttpEndpoint.getUser(request)
        )
                .withStatus(HttpStatus.NOT_FOUND);
    }


    @Test
    public void negativeIsRequired() {
        var request = GetUserRequestDto.builder()
                .playerId(null)
                .build();

        assertHttpFails(
                () -> playerHttpEndpoint.getUser(request)
        )
                .withStatus(HttpStatus.BAD_REQUEST);
    }

    private Integer createUserAndGetId(String role) {
        var request = CreateUserRequestDto.builder()
                .age(String.valueOf(TestUtils.generateAge()))
                .gender(TestUtils.generateGender())
                .login(TestUtils.generateLogin())
                .password(TestUtils.generatePassword())
                .role(role)
                .screenName(TestUtils.generateScreenName())
                .build();

        return playerHttpEndpoint.createUser(UserRole.SUPERVISOR.getRole(), request).getId();
    }
}
