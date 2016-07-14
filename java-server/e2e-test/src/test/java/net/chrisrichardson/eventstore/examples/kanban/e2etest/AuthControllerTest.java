package net.chrisrichardson.eventstore.examples.kanban.e2etest;

import net.chrisrichardson.eventstore.examples.kanban.testutil.AbstractAuthTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {E2ETestConfiguration.class})
@WebAppConfiguration
public class AuthControllerTest extends AbstractAuthTest {

    @Value("#{systemEnvironment['DOCKER_HOST_IP']}")
    private String hostName;

    @Value("#{systemEnvironment['DOCKER_PORT']}")
    private int port;

    @Override
    protected int getPort() {
        return this.port;
    }

    @Override
    protected String getHost() {
        return hostName;
    }
}
