package com.ebgr.pagamento_carnes.controller;

import com.ebgr.pagamento_carnes.controller.dto.TokenDTO;
import com.ebgr.pagamento_carnes.controller.dto.UserDTO;
import com.ebgr.pagamento_carnes.jwt.JwtUtil;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/public/logar")
    public ResponseEntity<String> logar() {
        return authenticate(new UserDTO(0, "erbert", null, "1234"));
    }


    @PostMapping("/api/user/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserDTO userDTO) {
        UserModel user = userService.authenticate(userDTO);
        TokenDTO token = JwtUtil.generateToken(user);
        return ResponseEntity
                .status(202)
                .header("Authorization", "Bearer " + token.token())
                .header("Set-Cookie", "accessToken="+token.token()+"; HttpOnly; Path=/; Max-Age="+token.maxAge())
                .body("Successfully logged as ("+user.getLogin()+").");
    }



    @PostMapping("/api/logout")
    private ResponseEntity<String> logout () {
        return ResponseEntity
                .status(200)
                .header("Authorization", "").header("Set-Cookie", "accessToken=; HttpOnly; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .body("Cookie destroyed successfully.");
    }
    @GetMapping("/api/logout")
    public ResponseEntity<String> logout_get() { return logout(); }
    @DeleteMapping("/api/logout")
    public ResponseEntity<String> logout_delete() { return logout(); }

    @PostMapping("/api/user/create")
    public ResponseEntity<UserModel> create(@RequestBody UserDTO userDTO) {
        UserModel user = userService.create(userDTO);
        return ResponseEntity.status(201).body(user);
    }

    @DeleteMapping("/api/user/{target}")
    public ResponseEntity<String> delete(@PathVariable String target) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.softDelete(authentication, target);

        return ResponseEntity
                .status(202)
                .header("Authorization", "")
                .body("User ("+target+") was successfully deleted.");
    }

    @GetMapping("/me")
    public ResponseEntity<UserModel> about_me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = (String) authentication.getPrincipal();

        UserModel user = userService.findUser(login);

        return ResponseEntity.status(200).body(user);
    }

    @GetMapping("/api/user/{login}")
    public ResponseEntity<UserModel> getInfo(@PathVariable String login) {
        UserModel user = userService.findUser(login);
        return ResponseEntity.status(200).body(user);
    }
}
