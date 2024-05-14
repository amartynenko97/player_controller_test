package player.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequestDto {

    public String age;
    public String gender;
    public String login;
    public String password;
    public String role;
    public String screenName;
}
