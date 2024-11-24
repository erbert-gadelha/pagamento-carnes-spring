package com.ebgr.pagamento_carnes.runner;


import com.ebgr.pagamento_carnes.controller.dto.UserDTO;
import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.jwt.JwtUtil;
import com.ebgr.pagamento_carnes.model.PaymentModel;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import com.ebgr.pagamento_carnes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Profile("dev")
@Component
public class RunnerDev implements CommandLineRunner {

    @Autowired
    private EfiHelper efiHelper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public void run(String... args) throws Exception {
        feedRepository();
    }

    private void feedRepository() {

        userService.create(new UserDTO(null, "eric", "Eric Rocha", "1234"));
        UserModel erbert = userService.create(new UserDTO(null, "erbert", "Erbert Gadelha", "1234"));


        List<UserModel> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user.toString()));



        //for(int i = 0; i < 12; i++)
        for(int i = 0; i < 5; i++) {
            PaymentModel paymentModel = new PaymentModel(erbert, i, 2024);
            paymentModel.setClosedAt(LocalDateTime.now());
            paymentRepository.save(paymentModel);
        }


        //List<Payment> payments = paymentRepository.findAll();
        //payments.forEach(payment -> System.out.println(payment.toString()));
    }

    private void testPixApi() {
        efiHelper.exibirListaDeCobrancas();
        CobrancaImediata.Response cobrancaImediata = efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert Gadelha"), 0.25f, 3600);
        if(cobrancaImediata != null) {
            GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);
            System.out.println("\n\nqrCode: " + qrCode);
        }
    }


}