package com.ebgr.pagamento_carnes.notifications;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import jakarta.websocket.PongMessage;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private HashMap<String, WebSocketSession> sessions = new HashMap<>(100);


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Associe um identificador (ex.: ID do usuário) à sessão
        System.out.println("handler: " + session.getId() + " | " + session.toString());
        //String userId = getUserIdFromSession(session);
        String userId = String.valueOf(new Random().nextInt(100));
        sessions.put(userId, session);

        sendMessageAll("Alguém entrou.");
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remova a sessão quando o cliente desconectar
        System.out.println("closing: " + session.getId() + " | " + session.toString());
        //String userId = getUserIdFromSession(session);
        String userId = String.valueOf(new Random().nextInt(100));
        sessions.put(userId, session);
        sessions.remove(userId);

        sendMessageAll("Alguém saiu.");
    }

    @Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("handleTextMessage: " + message);
        replicate(session, message.getPayload());
	}
    @Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("handleMessage: " + message);
        String content = message.getPayload().toString();
        replicate(session, content);
    }
    @Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        System.out.println("handleBinaryMessage: " + message);
	}


    private void _sendMessage(WebSocketSession session, String message) {
        if (session == null | !session.isOpen())
            return;            
        try {
            session.sendMessage(new TextMessage(message));
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }


        System.out.println("send message: " + message);
    }

    public void replicate(WebSocketSession session, String message) {
        for(WebSocketSession _session : sessions.values()) {
            if(_session != session) {
                System.out.println("Iguais");
                _sendMessage(_session, message);
            } else {
                System.out.println("Diferentes");
            }
        }
    }

    public void sendMessage(String userId, String message) {
        WebSocketSession session = sessions.get(userId);
        _sendMessage(session, message);
    }

    public void sendMessageAll(String message) {
        for(WebSocketSession session : sessions.values()) {
            _sendMessage(session, message);
        }
    }


    public MyWebSocketHandler() {
        System.err.println("\n\n\n\nn\n\n\n\n\nn\n\n\nWASD\n\n\n\n\nn\n\n\n\n\nn\n\n");
    }
}
