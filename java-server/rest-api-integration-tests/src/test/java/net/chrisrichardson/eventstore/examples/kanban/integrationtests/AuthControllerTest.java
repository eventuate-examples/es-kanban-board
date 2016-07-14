package net.chrisrichardson.eventstore.examples.kanban.integrationtests;

import net.chrisrichardson.eventstore.examples.kanban.testutil.AbstractAuthTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {RestAPITestConfiguration.class})
@WebAppConfiguration
public class AuthControllerTest extends AbstractAuthTest {

    @Value("${local.server.port}")
    private int port;

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected String getHost() {
        return "localhost";
    }
}
