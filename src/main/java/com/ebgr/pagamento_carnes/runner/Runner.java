package com.ebgr.pagamento_carnes.runner;

import com.ebgr.pagamento_carnes.controller.dto.UserDTO;
import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.jwt.JwtUtil;
import com.ebgr.pagamento_carnes.model.PaymentModel;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import com.ebgr.pagamento_carnes.service.PaymentService;
import com.ebgr.pagamento_carnes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {


    @Value("${spring.profiles.active}")
    private String activeProfile;


    @Autowired
    private EfiHelper efiHelper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${application.domain:}")
    String appDomain;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;



    @Override
    public void run(String... args) throws Exception {
        JwtUtil.setDomain(appDomain);

        //if(activeProfile.equals("prd"))
        //    getUsers();
        if(activeProfile.equals("dev"))
            feedRepository();


        List<PaymentModel> paymentModels = paymentRepository.findAllByClosedAtIsNull();
        System.out.println(paymentModels);
        new Thread( () -> paymentModels.forEach(paymentService::verifyPayment)).start();
    }

    private void feedRepository() {
        userService.create(new UserDTO(null, "eric", "Eric Rocha", "1234"));
        UserModel erbert = userService.create(new UserDTO(null, "erbert", "Erbert Gadelha", "1234"));

        List<UserModel> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user.toString()));

        for(int i = 0; i < 5; i++) {
            PaymentModel paymentModel = new PaymentModel(erbert, i, 2024);
            if(i < 3)
                paymentModel.setClosedAt(LocalDateTime.now());
            paymentRepository.save(paymentModel);
        }
    }
}
