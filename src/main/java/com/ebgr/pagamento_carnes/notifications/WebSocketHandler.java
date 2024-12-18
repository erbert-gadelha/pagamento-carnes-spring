package com.ebgr.pagamento_carnes.notifications;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ebgr.pagamento_carnes.notifications.dto.MessageDTO;

//@Component
public class WebSocketHandler /*extends TextWebSocketHandler*/ {
    /*private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Associe um identificador (ex.: ID do usuário) à sessão
        System.out.println("WebSocketHandler.afterConnectionEstablished");
        String userId = getUserIdFromSession(session);
        sessions.put(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Lógica para processar mensagens recebidas
        System.out.println("Mensagem recebida: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remova a sessão quando o cliente desconectar
        String userId = getUserIdFromSession(session);
        sessions.remove(userId);
    }

    public void sendMessageToUser(MessageDTO message) throws Exception {
        WebSocketSession session = sessions.get(message.sender());
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message.message()));
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        // Extraia o ID do usuário (ex.: de um token ou parâmetro da URL)
        return session.getUri().getQuery().split("=")[1]; // Exemplo simples
    }*/
}
