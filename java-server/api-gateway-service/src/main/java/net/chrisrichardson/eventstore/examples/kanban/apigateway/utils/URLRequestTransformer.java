package net.chrisrichardson.eventstore.examples.kanban.apigateway.utils;

import net.chrisrichardson.eventstore.examples.kanban.apigateway.ApiGatewayProperties;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

public class URLRequestTransformer extends ProxyRequestTransformer {

  private ApiGatewayProperties apiGatewayProperties;

  public URLRequestTransformer(ApiGatewayProperties apiGatewayProperties) {
    this.apiGatewayProperties = apiGatewayProperties;
  }

  @Override
  public RequestBuilder transform(HttpServletRequest request) throws NoHandlerFoundException, URISyntaxException {
    String requestURI = request.getRequestURI();
    URI uri;
    if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
      uri = new URI(getServiceUrl(requestURI, request) + "?" + request.getQueryString());
    } else {
      uri = new URI(getServiceUrl(requestURI, request));
    }

    RequestBuilder rb = RequestBuilder.create(request.getMethod());
    rb.setUri(uri);
    return rb;
  }

  private String getServiceUrl(String requestURI, HttpServletRequest httpServletRequest) throws NoHandlerFoundException {

    ApiGatewayProperties.Endpoint endpoint =
            apiGatewayProperties.getEndpoints().stream()
                    .filter(e ->
                            requestURI.matches(e.getPath()) && e.getMethod() == RequestMethod.valueOf(httpServletRequest.getMethod())
                    )
                    .findFirst().orElseThrow(() -> new NoHandlerFoundException(httpServletRequest.getMethod(),
                    httpServletRequest.getRequestURL().toString(), new HttpHeaders()));
    return endpoint.getLocation() + requestURI;
  }
}
