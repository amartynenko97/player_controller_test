package player.test.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.url}")
    private String url;

    @Value("${app.threadCount}")
    private int threadCount;

    public String getUrl() {
        return url;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
