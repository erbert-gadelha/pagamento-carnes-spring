package com.ebgr.pagamento_carnes.service;

import com.ebgr.pagamento_carnes.model.User;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    @Lazy
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findUserByLogin(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found.");
        return new UserDetailsImpl(user.get());
    }

    public void createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }
}