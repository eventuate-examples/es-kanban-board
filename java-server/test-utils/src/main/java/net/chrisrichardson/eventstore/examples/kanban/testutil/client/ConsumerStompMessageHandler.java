package net.chrisrichardson.eventstore.examples.kanban.testutil.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by popikyardo on 23.12.15.
 */
public class ConsumerStompMessageHandler implements StompMessageHandler {

    private final int expectedMessageCount;

    private final CountDownLatch connectLatch;

    private final CountDownLatch subscribeLatch;

    private final CountDownLatch messageLatch;

    private final CountDownLatch disconnectLatch;

    private final AtomicReference<Throwable> failure;

    private StompSession stompSession;

    private AtomicInteger messageCount = new AtomicInteger(0);

    private static Log logger = LogFactory.getLog(ConsumerStompMessageHandler.class);


    public ConsumerStompMessageHandler(int expectedMessageCount, CountDownLatch connectLatch,
                                       CountDownLatch subscribeLatch, CountDownLatch messageLatch, CountDownLatch disconnectLatch,
                                       AtomicReference<Throwable> failure) {

        this.expectedMessageCount = expectedMessageCount;
        this.connectLatch = connectLatch;
        this.subscribeLatch = subscribeLatch;
        this.messageLatch = messageLatch;
        this.disconnectLatch = disconnectLatch;
        this.failure = failure;
    }


    @Override
    public void afterConnected(StompSession stompSession, StompHeaderAccessor headers) {
        this.connectLatch.countDown();
        this.stompSession = stompSession;
        stompSession.subscribe("/topic/greeting", "receipt1");
    }

    @Override
    public void handleReceipt(String receiptId) {
        this.subscribeLatch.countDown();
    }

    @Override
    public void handleMessage(Message<byte[]> message) {
        if (this.messageCount.incrementAndGet() == this.expectedMessageCount) {
            this.messageLatch.countDown();
            this.stompSession.disconnect();
        }
    }

    @Override
    public void handleError(Message<byte[]> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String error = "[Consumer] " + accessor.getShortLogMessage(message.getPayload());
        logger.error(error);
        this.failure.set(new Exception(error));
    }

    @Override
    public void afterDisconnected() {
        logger.trace("Disconnected in " + this.stompSession);
        this.disconnectLatch.countDown();
    }

    @Override
    public String toString() {
        return "ConsumerStompMessageHandler[messageCount=" + this.messageCount + ", " + this.stompSession +  "]";
    }
}
