
package net.chrisrichardson.eventstore.examples.kanban.e2etest;

import net.chrisrichardson.eventstore.examples.kanban.testutil.AbstractStompApiTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = E2ETestConfiguration.class,
        properties = "server.port=0",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StompApiTest extends AbstractStompApiTest {

    @Value("#{systemEnvironment['DOCKER_HOST_IP']}")
    private String hostName;

    @Autowired
    private ApplicationContext applicationContext;

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

    @Override
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}