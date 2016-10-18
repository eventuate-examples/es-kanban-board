package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

  private final Logger logger = LoggerFactory.getLogger(PresenceChannelInterceptor.class);

  @Override
  public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

    StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

    // ignore non-STOMP messages like heartbeat messages
    if(sha.getCommand() == null) {
      if(sha.isHeartbeat()) {
        logger.info("STOMP heartbeat");
      }
      return;
    }

    String sessionId = sha.getSessionId();

    switch(sha.getCommand()) {
      case CONNECT:
        logger.info("STOMP Connect [sessionId: " + sessionId + "]");
        break;
      case CONNECTED:
        logger.info("STOMP Connected [sessionId: " + sessionId + "]");
        break;
      case DISCONNECT:
        logger.info("STOMP Disconnect [sessionId: " + sessionId + "]");
        break;
      case ACK:
        logger.info("STOMP Ack [sessionId: " + sessionId + "]");
        break;
      case NACK:
        logger.info("STOMP Nack [sessionId: " + sessionId + "]");
        break;
      default:
        break;

    }
  }
}