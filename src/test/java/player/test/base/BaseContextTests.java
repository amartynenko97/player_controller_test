package player.test.base;

import io.qameta.allure.testng.AllureTestNg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import player.test.config.AppConfig;
import player.test.endpoints.PlayerHttpEndpoint;

@SpringBootTest
@Listeners({AllureTestNg.class})
public class BaseContextTests extends AbstractTestNGSpringContextTests {

    @Autowired
    protected PlayerHttpEndpoint playerHttpEndpoint;

    @Autowired
    protected AppConfig appConfig;

//    @BeforeClass
//    public void setUp() {
//
//    }
}
