package net.chrisrichardson.eventstore.examples.kanban.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.model.AuthRequest;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.model.AuthResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest({"server.port=0"})
public abstract class AbstractAuthTest {

    protected int port;

    private String baseUrl(String path) {
        return "http://"+getHost()+":" + getPort() + "/" + path;
    }

    @Autowired
    RestTemplate restTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldFailWithoutToken() throws IOException {
        try {
            Request.Get(baseUrl("/")).execute();
        } catch(HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
        }
    }

    @Test
    public void shouldSuccessWithToken() throws IOException {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@test.com");
        HttpResponse authResp = Request.Post(baseUrl("api/authenticate"))
                .bodyString(mapper.writeValueAsString(authRequest), ContentType.APPLICATION_JSON)
                .execute().returnResponse();

        Assert.assertEquals(HttpStatus.OK.value(), authResp.getStatusLine().getStatusCode());
        String content = EntityUtils.toString(authResp.getEntity());
        Assert.assertNotNull(content);
        AuthResponse authResponse = mapper.readValue(content, AuthResponse.class);
        Assert.assertFalse(authResponse.getToken().isEmpty());
    }

    protected abstract int getPort();

    protected abstract String getHost();
}
