package player.test.asserter;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class WebClientException extends RuntimeException {

    @Getter
    private final HttpStatus statusCode;
    private final String responseBody;

    public WebClientException(HttpStatus statusCode, String responseBody) {
        super("HTTP Status " + statusCode + ": " + responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public String getResponseBodyAsString() {
        return responseBody;
    }
}
