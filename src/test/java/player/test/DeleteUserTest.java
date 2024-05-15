package player.test;

import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import player.test.base.BaseContextTests;
import player.test.dto.CreateUserRequestDto;
import player.test.dto.DeleteUserRequestDto;
import player.test.dto.PlayerDto;
import player.test.statics.UserRole;
import player.test.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static player.test.asserter.WebClientErrorAsserter.assertHttpFails;

public class DeleteUserTest extends BaseContextTests {

    @DataProvider(name = "userRoles")
    public Object[][] userRoles() {
        return new Object[][]{
                {UserRole.SUPERVISOR.getRole(), UserRole.USER.getRole()},
                {UserRole.SUPERVISOR.getRole(), UserRole.ADMIN.getRole()},
                {UserRole.ADMIN.getRole(), UserRole.USER.getRole()},
                {UserRole.ADMIN.getRole(), UserRole.ADMIN.getRole()}
        };
    }

    @Test(dataProvider = "userRoles")
    public void positive(String editorRole, String userRole) {
        var userId = createUserAndGetId(userRole);
        var request = DeleteUserRequestDto.builder()
                .playerId(userId)
                .build();

        playerHttpEndpoint.deleteUser(request, editorRole);
        // Check in database
        var playersAfterDeletion = playerHttpEndpoint.getAllPlayers();

        assertThat(playersAfterDeletion)
                .extracting(PlayerDto::getId)
                .doesNotContain(String.valueOf(userId));

    }


    @Test
    public void negativeUpdateUserWithoutPermission() {
        var userId = createUserAndGetId(UserRole.ADMIN.getRole());

        var request = DeleteUserRequestDto.builder()
                .playerId(userId)
                .build();

        assertHttpFails(
                () -> playerHttpEndpoint.deleteUser(request, UserRole.USER.getRole())
        )
                .withStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    public void negativeNotExistUserId() {
        var request = DeleteUserRequestDto.builder()
                .playerId(TestUtils.generateAge())
                .build();

        assertHttpFails(
                () -> playerHttpEndpoint.deleteUser(request, UserRole.USER.getRole())
        )
                .withStatus(HttpStatus.FORBIDDEN);
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
