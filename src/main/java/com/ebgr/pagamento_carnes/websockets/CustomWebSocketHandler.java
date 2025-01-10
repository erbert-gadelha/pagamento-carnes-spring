package com.ebgr.pagamento_carnes.websockets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import com.ebgr.pagamento_carnes.websockets.dto.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class CustomWebSocketHandler implements WebSocketHandler {

    // Armazena as conexões por sessão ID
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // Armazena os tópicos e os assinantes
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> topics = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("Conexão estabelecida: " + session.getId());
    }

    private final ObjectMapper objectMapper = new ObjectMapper();


    private String objectToJson(MessageDTO messageDTO) {
        try {
            return objectMapper.writeValueAsString(messageDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MessageDTO jsonToMessageDTO (String json) {
        try {
            return (MessageDTO) objectMapper.readValue(json, MessageDTO.class);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        String payload = message.getPayload().toString();
        System.out.println("Mensagem recebida: " + payload);

        String[] parts = payload.split(" ", 3);
        if(parts.length < 2)
            return;

        String action = parts[0];
        String topic = parts[1];


        switch (action) {
            case "SUBSCRIBE":
                topics.computeIfAbsent(topic, k -> new CopyOnWriteArraySet<>()).add(session);
                System.out.println("Usuário inscrito no tópico: " + topic);                
                break;
            case "SEND":
                MessageDTO messageDTO = jsonToMessageDTO(parts[2]);
                sendToAll(topic, objectToJson(messageDTO));
                break;
        
            default:
                break;
        }
    }

    @Autowired
    private MessageService messageService;

    private void sendToAll(String topic, String message) throws Exception  {
        if(topic == null || message == null)
            return;

        messageService.writeMessage(message);

        CopyOnWriteArraySet<WebSocketSession> subscribers = topics.getOrDefault(topic, new CopyOnWriteArraySet<>());
        for (WebSocketSession subscriber : subscribers) {
            if (subscriber.isOpen())
                subscriber.sendMessage(new TextMessage(message));                        
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("Erro no WebSocket: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        topics.values().forEach(subscribers -> subscribers.remove(session));
        System.out.println("Conexão encerrada: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
