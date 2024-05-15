package player.test.asserter;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class WebClientError {
    private HttpStatus status;
    private List<WebClientErrorPayload> descriptions;

    public WebClientError(HttpStatus status) {
        this.status = status;
    }
}
