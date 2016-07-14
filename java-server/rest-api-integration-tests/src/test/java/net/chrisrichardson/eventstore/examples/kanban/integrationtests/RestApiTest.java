package net.chrisrichardson.eventstore.examples.kanban.integrationtests;

import net.chrisrichardson.eventstore.examples.kanban.testutil.AbstractRestApiTest;
import org.springframework.context.ApplicationContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {RestAPITestConfiguration.class})
@IntegrationTest({"server.port=0"})
public class RestApiTest extends AbstractRestApiTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected String getHost() {
        return "localhost";
    }

    @Override
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}