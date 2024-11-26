package com.ebgr.pagamento_carnes.service;

import com.ebgr.pagamento_carnes.controller.dto.UserDTO;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    private final int deletedUser_lifeDays = 30;




    public UserModel authenticate(UserDTO userDTO) {
        UserModel user = userRepository.findActiveUserByLogin(userDTO.login()).orElse(null);
        if(user != null && BCryptUtil.checkpw(userDTO.password(), user.getPassword()))
            return sanitizedUser(user);
        throw new RuntimeException("Bad credentials.");
    }

    public UserModel create(UserDTO userDTO){
        if(!validateDTO(userDTO))
            return null;


        String hashed_pw = BCryptUtil.hashpw(userDTO.password());
        UserModel user = new UserModel(userDTO.name(), userDTO.login(), hashed_pw);

        try {
            return sanitizedUser(userRepository.save(user));
        } catch (Exception e) {
            throw new RuntimeException("\"login\" field must be unique.");
        }
    }

    public UserModel findUser(String userLogin){
        UserModel user = userRepository.findUserByLogin(userLogin).orElse(null);
        return sanitizedUser(user);
    }


    @Transactional
    public void softDelete(Authentication auth, String delete) {
        if(!auth.getPrincipal().equals(delete))
            throw new RuntimeException("Bad credentials.");
        UserModel deleteUser = userRepository.findActiveUserByLogin(delete).orElseThrow(
            () -> new RuntimeException("User (" + delete + ") not found.")
        );
        deleteUser.setDeletedAt(LocalDateTime.now());
        userRepository.save(deleteUser);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldUsers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(deletedUser_lifeDays);
        List<UserModel> usersToDelete = userRepository.findAllByDeletedAtBefore(thirtyDaysAgo);
        userRepository.deleteAll(usersToDelete);
    }



    private UserModel sanitizedUser(UserModel userModel) {
        if(userModel == null)
            return new UserModel();

        userModel.setPassword(null);
        return userModel;
    }

    private Boolean validateDTO(UserDTO userDTO) {
        if(userDTO.name() == null || userDTO.name().length() < 3)
            throw new RuntimeException("\"name\" must contain between 3 and 50 characters");
        if(userDTO.login() == null || userDTO.login().length() < 3)
            throw new RuntimeException("\"login\" must contain between 3 and 50 characters");
        if(userDTO.password() == null || userDTO.password().length() < 4)
            throw new RuntimeException("\"password\" must contain between 3 and 50 characters");

        return true;

    }
}
