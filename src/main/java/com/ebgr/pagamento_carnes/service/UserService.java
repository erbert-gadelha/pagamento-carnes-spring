package com.ebgr.pagamento_carnes.service;

import com.ebgr.pagamento_carnes.controller.dto.Login;
import com.ebgr.pagamento_carnes.model.User;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public User tryToLogin(String login, String password) {
        User user = repository.findUserByLogin(login).orElse(null);
        if(user != null)
            if(user.getPassword().equals(password))
                return user;
            else
                return  null;

        System.err.printf("Usuário (%s) não encontrado.\n", login);
        return null;
    }
}
