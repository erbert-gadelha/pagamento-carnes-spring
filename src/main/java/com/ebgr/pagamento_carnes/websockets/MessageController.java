package com.ebgr.pagamento_carnes.websockets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ebgr.pagamento_carnes.controller.dto.WebhookDTO;
import com.ebgr.pagamento_carnes.websockets.dto.MessageDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class MessageController {

  @Autowired
  private MessageService messageService;


  @GetMapping("/api/v1/messages/{last_id}")
  public List<String> getMessages(@PathVariable int last_id) throws Exception {
    List<String> messages = messageService.readMessages(last_id);
    return messages;
  }
  
  @GetMapping("/api/v1/messages")
  public int getMessagesSize () throws Exception {
    return messageService.getLast();
  }
    
}
