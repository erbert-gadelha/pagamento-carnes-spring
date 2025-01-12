package com.ebgr.pagamento_carnes.websockets;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class MessageService {

    private final String path = "files/arquivo.txt";
    private final int maxRead = 5;
    private int size;

    public MessageService() {
        this.updateSize();
    }

    private void updateSize() {
        try {
            Path path = Paths.get(this.path);
            this.size = Files.readAllLines(path).size();
        } catch (IOException e) {
            this.size = 0; // Arquivo não encontrado ou vazio
        }
    }

    private int clamp(int min, int value, int max) {
        return Math.max(min, Math.min(max, value));
    }


    /*ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(String jsonString) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(jsonString, Map.class);
            System.out.println(jsonMap);            
            return objectMapper.writeValueAsString(jsonMap);}
        catch(Exception e) { }
        return "{}";
    }*/

    public synchronized List<String> readMessages(int lastId) {
        if(lastId < 0)
            return Collections.emptyList();

        try {
            Path path = Paths.get(this.path);
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                return Collections.emptyList();
            }

            int firstId = Math.max(0, lastId + 1 - this.maxRead);
            lastId = clamp(0, lastId, lines.size() - 1);

            List<String> messages = new ArrayList<>(lines.subList(firstId, lastId+1));
            return messages;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public synchronized void writeMessage(String message) {
        try {
            Path path = Paths.get(this.path);

            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            Files.write(path, List.of(message), StandardOpenOption.APPEND);
            this.size++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLast() {
        return this.size;
    }
}
