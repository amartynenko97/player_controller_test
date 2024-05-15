package player.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponseDto {

    public Integer age;
    public String gender;
    public Integer id;
    public String login;
    public String password;
    public String role;
    public String screenName;
}
