package player.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserRequestDto {
    public String playerId;
}
