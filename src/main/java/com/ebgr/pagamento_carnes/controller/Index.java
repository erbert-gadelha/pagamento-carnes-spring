package com.ebgr.pagamento_carnes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
//@CrossOrigin(origins = "*") // Permite todas as origens
public class Index {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("status")
    public ResponseEntity<String> status() {
        return ResponseEntity
                .ok()
                .body("Container is up.");
    }
}

