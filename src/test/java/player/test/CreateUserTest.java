package player.test;

import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import player.test.base.BaseContextTests;
import player.test.dto.CreateUserRequestDto;
import player.test.statics.UserRole;
import player.test.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static player.test.asserter.WebClientErrorAsserter.assertHttpFails;

public class CreateUserTest extends BaseContextTests {

    @DataProvider(name = "userRoles")
    public Object[][] userRoles() {
        return new Object[][]{
                {UserRole.ADMIN.getRole(), UserRole.USER.getRole()},
                {UserRole.SUPERVISOR.getRole(), UserRole.ADMIN.getRole()},
                {UserRole.SUPERVISOR.getRole(), UserRole.USER.getRole()}
        };
    }

    @Test(dataProvider = "userRoles")
    public void positive(String editorRole, String userRole) {
        var login = TestUtils.generateLogin();
        var request = buildCreateUserRequest(login, userRole);

        var response = playerHttpEndpoint.createUser(editorRole, request);
        assertThat(response.getLogin()).isEqualTo(request.getLogin());
        assertThat(response.getId()).isNotNull();
    }


    @Test
    public void negativeUserWithoutPermissionTest() {
        var login = TestUtils.generateLogin();
        var role = UserRole.SUPERVISOR.getRole();
        var request = buildCreateUserRequest(login, role);

        assertHttpFails(
                () -> playerHttpEndpoint.createUser(UserRole.USER.getRole(), request)
        )
                .withStatus(HttpStatus.FORBIDDEN);

    }

    @Test
    public void negativeUserWhoAlreadyExist() {
        var login = TestUtils.generateLogin();
        var role = UserRole.USER.getRole();

        var initialRequest = buildCreateUserRequest(login, role);
        playerHttpEndpoint.createUser(UserRole.SUPERVISOR.getRole(), initialRequest);

        var request = buildCreateUserRequest(login, role);
        assertHttpFails(
                () -> playerHttpEndpoint.createUser(UserRole.SUPERVISOR.getRole(), request)
        )
                .withStatus(HttpStatus.BAD_REQUEST);
    }


    private CreateUserRequestDto buildCreateUserRequest(String login, String role) {
        return CreateUserRequestDto.builder()
                .age(String.valueOf(TestUtils.generateAge()))
                .gender(TestUtils.generateGender())
                .login(login)
                .password(TestUtils.generatePassword())
                .role(role)
                .screenName(TestUtils.generateScreenName())
                .build();
    }
}
