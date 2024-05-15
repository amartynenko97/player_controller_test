package player.test.asserter;

import lombok.Data;

@Data
public class WebClientErrorPayload {
    private String code;
    private String message;
    private String field;
    private Object params;
}