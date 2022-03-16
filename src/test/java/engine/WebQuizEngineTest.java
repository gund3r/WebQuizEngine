package engine;

import engine.controllers.QuizController;
import engine.controllers.UserController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WebQuizEngineTest {

    @Autowired
    private QuizController quizController;

    @Autowired
    private UserController userController;

    @LocalServerPort
    private final int port = 8889;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api",
                String.class)).contains("Welcome to QuizService");
    }


}

