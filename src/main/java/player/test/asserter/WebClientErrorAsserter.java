package player.test.asserter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.SneakyThrows;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ThrowableAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class WebClientErrorAsserter extends AbstractAssert<WebClientErrorAsserter, WebClientError> {

    private static final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    public WebClientErrorAsserter(WebClientError e) {
        super(e, WebClientErrorAsserter.class);
    }

    @SneakyThrows
    public static WebClientErrorAsserter assertHttpFails(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        try {
            shouldRaiseThrowable.call();
        } catch (WebClientException ex) {
            var webError = new WebClientError(ex.getStatusCode());
            try {
                var errors = objectMapper.readValue(ex.getResponseBodyAsString(), new TypeReference<List<WebClientErrorPayload>>() {});
                webError.setDescriptions(errors);
            } catch (MismatchedInputException exx) {
                try {
                    var error = objectMapper.readValue(ex.getResponseBodyAsString(), WebClientErrorPayload.class);
                    webError.setDescriptions(Collections.singletonList(error));
                } catch (MismatchedInputException ignore) {

                }
            }
            return new WebClientErrorAsserter(webError);
        }
        return failBecauseExceptionWasNotThrown(RuntimeException.class);
    }

    public WebClientErrorAsserter withStatus(HttpStatus status) {
        assertThat(actual.getStatus()).isEqualTo(status);
        return this;
    }
}