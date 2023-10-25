package com.karl.chatweb.websocket;

import com.karl.chatweb.ChatWebApplication;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChatWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompWebsocketTests {

    @LocalServerPort
    private int port;

    static final String WEBSOCKET_URI = "ws://localhost:{port}/websocket";
    static final String WEBSOCKET_TOPIC = "/topic";

    static BlockingQueue<String> blockingQueue;
    static WebSocketStompClient stompClient;


    @BeforeAll
    public static void setup() {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
    }

    @Test
    public void StompWebsockets_SendMessageToServer_ShouldReceiveAMessageFromTheServer() throws Exception {
        StompSession session = stompClient
                .connect(WEBSOCKET_URI.replace("{port}", String.valueOf(port)), new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);
        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        String message = "MESSAGE TEST";
        session.send(WEBSOCKET_TOPIC, message.getBytes());

        assertThat(blockingQueue.poll(5, SECONDS)).isEqualTo(message);
    }

    static class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public @NotNull Type getPayloadType(@NotNull StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(@NotNull StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }
}
