package net.chrisrichardson.eventstore.examples.kanban.testutil.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.KanbanWebSocketEvent;
import net.chrisrichardson.eventstore.examples.kanban.testutil.client.StompMessageHandler;
import net.chrisrichardson.eventstore.examples.kanban.testutil.client.StompSession;
import net.chrisrichardson.eventstore.examples.kanban.testutil.client.WebSocketStompClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StompListener {

  private String token;
  private String destination;
  private String host;
  private int port;

  private ObjectMapper mapper = new ObjectMapper();

  private List<KanbanWebSocketEvent> events = new ArrayList<>();

  private static Log log = LogFactory.getLog(StompListener.class);

  public StompListener(String token, String destination, String host, int port) {
    this.token = token;
    this.destination = destination;
    this.host = host;
    this.port = port;

    initializeStompClient();
  }

  private void initializeStompClient() {
    WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    final AtomicReference<Throwable> failure = new AtomicReference<>();

    List<Transport> transports = new ArrayList<>();
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    transports.add(new RestTemplateXhrTransport(new RestTemplate()));

    StompMessageHandler handler = new StompMessageHandler() {

      private StompSession stompSession;

      @Override
      public void afterConnected(StompSession stompSession, StompHeaderAccessor headers) {
        this.stompSession = stompSession;
        this.stompSession.subscribe(destination, null);

      }

      @Override
      public void handleMessage(Message<byte[]> message) {
        String json = new String(message.getPayload());
        try {
          events.add(mapper.readValue(json, KanbanWebSocketEvent.class));
        } catch (IOException e) {
          new RuntimeException(e);
        }
      }

      @Override
      public void handleError(Message<byte[]> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String error = "[Producer] " + accessor.getShortLogMessage(message.getPayload());
        log.error(error);
        failure.set(new Exception(error));
      }

      @Override
      public void handleReceipt(String receiptId) {
      }

      @Override
      public void afterDisconnected() {
      }
    };

    try {
      URI uri = new URI("http://" + host + ":" + port + "/events");
      WebSocketStompClient stompClient = new WebSocketStompClient(uri, headers, new SockJsClient(transports));
      stompClient.setMessageConverter(new MappingJackson2MessageConverter());
      headers.add("x-access-token", token);
      stompClient.connect(handler);

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

  }

  public List<KanbanWebSocketEvent> getEvents() {
    return events;
  }
}