package player.test;

import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import player.test.base.BaseContextTests;
import player.test.dto.CreateUserRequestDto;
import player.test.dto.UpdateUserRequestDto;
import player.test.dto.UpdateUserResponseDto;
import player.test.statics.UserRole;
import player.test.utils.TestUtils;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static player.test.asserter.WebClientErrorAsserter.assertHttpFails;

public class UpdateUserTest extends BaseContextTests {

    @DataProvider(name = "updateUserFields")
    public Object[][] updateUserFields() {
        return new Object[][]{
                {"age", String.valueOf(TestUtils.generateAge())},
                {"gender", TestUtils.generateGender()},
                {"login", TestUtils.generateLogin()},
                {"password", TestUtils.generatePassword()},
                {"screenName", TestUtils.generateScreenName()}
        };
    }

    @Test(dataProvider = "updateUserFields")
    public void positive(String field, String value) {
        var role = UserRole.USER.getRole();
        var userId = createUserAndGetId(role);
        var editor = UserRole.SUPERVISOR.getRole();

        var updateUserRequest = buildUpdateUserRequest(field, value);
        var updateResponse = playerHttpEndpoint.updateUser(updateUserRequest, userId, editor);

        assertThat(updateResponse).isNotNull();
        assertThat(getFieldValue(updateResponse, field)).isEqualTo(value);
    }

    @Test
    public void negativeUpdateUserWithoutPermission() {
        var userId = createUserAndGetId(UserRole.ADMIN.getRole());

        var updateUserRequest = UpdateUserRequestDto.builder()
                .login(TestUtils.generateLogin())
                .build();

        assertHttpFails(
                () -> playerHttpEndpoint.updateUser(updateUserRequest, userId, UserRole.USER.getRole())
        )
                .withStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    public void negativeNotExistUserId() {
        var updateUserRequest = UpdateUserRequestDto.builder()
                .role(UserRole.SUPERVISOR.getRole())
                .build();

        assertHttpFails(
                () -> playerHttpEndpoint.updateUser(updateUserRequest, TestUtils.generateAge(), UserRole.SUPERVISOR.getRole())
        )
                .withStatus(HttpStatus.NOT_FOUND);
    }


    private UpdateUserRequestDto buildUpdateUserRequest(String field, String value) {
        UpdateUserRequestDto.UpdateUserRequestDtoBuilder builder = UpdateUserRequestDto.builder();
        switch (field) {
            case "age":
                builder.age(value);
            case "gender":
                builder.gender(value);
            case "login":
                builder.login(value);
            case "password":
                builder.password(value);
            case "screenName":
                builder.screenName(value);
        }
        return builder.build();
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

    private String getFieldValue(UpdateUserResponseDto response, String field) {
        try {
            Method method = response.getClass().getMethod("get" + capitalize(field));
            return method.invoke(response).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field value", e);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
