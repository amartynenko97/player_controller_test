package player.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUserRequestDto {
    public String editor;
    public String playerId;
}
