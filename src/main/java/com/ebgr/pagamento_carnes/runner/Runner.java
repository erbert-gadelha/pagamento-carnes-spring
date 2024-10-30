package com.ebgr.pagamento_carnes.runner;


import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.model.Payment;
import com.ebgr.pagamento_carnes.model.User;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Profile("dev")
@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private EfiHelper efiHelper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void run(String... args) throws Exception {
        feedRepository();
    }

    private void feedRepository() {
        /*User user1 = new User("user", "1234");
        User user2 = new User("usuario", "senha");
        System.out.println(user1);
        System.out.println(user2);
        user1.setPassword(encoder.encode(user1.getPassword()));
        user2.setPassword(encoder.encode(user2.getPassword()));
        repository.save(user1);
        repository.save(user2);*/

        //User user_ = new User("user", "1234");
        User eric = new User("Eric", "1234");
        User erbert = new User("Erbert", "1234");
        //userRepository.save(user_);
        userRepository.save(eric);
        userRepository.save(erbert);

        List<User> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user.toString()));



        //for(int i = 0; i < 12; i++)
        for(int i = 0; i < 5; i++) {
            Payment payment = new Payment(erbert, i, 2024);
            payment.setClosedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        }


        //List<Payment> payments = paymentRepository.findAll();
        //payments.forEach(payment -> System.out.println(payment.toString()));
    }

    private void testPixApi() {
        efiHelper.consultarListaDeCobrancas();
        CobrancaImediata.Response cobrancaImediata = efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert Gadelha"), 0.25f, 3600);
        if(cobrancaImediata != null) {
            GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);
            System.out.println("\n\nqrCode: " + qrCode);
        }
    }


}