
package net.chrisrichardson.eventstore.examples.kanban.e2etest;

import net.chrisrichardson.eventstore.examples.kanban.testutil.AbstractStompApiTest;
import org.springframework.context.ApplicationContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;



/**
 * Created by Main on 06.10.2015.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {E2ETestConfiguration.class})
@IntegrationTest({"server.port=0"})
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