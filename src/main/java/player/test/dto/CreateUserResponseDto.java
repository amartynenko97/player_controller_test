package player.test.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseDto {

    public Integer age;
    public String gender;
    public Integer id;
    public String login;
    public String password;
    public String role;
    public String screenName;
}
