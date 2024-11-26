package com.ebgr.pagamento_carnes.runner;

import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("prd")
@Component
public class RunnerPrd implements CommandLineRunner {

    @Autowired
    private EfiHelper efiHelper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void run(String... args) throws Exception {
        getUsers();
        /*var cobranca =  efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Fulano da Silva"), 0.25, 60);
        var qrCode = efiHelper.criarQrCode(cobranca);
        System.out.println("Link da cobranca: " + qrCode.linkVisualizacao());*/
    }

    private void getUsers() {
        List<UserModel> users = userRepository.findAll();
        System.out.printf("Users <%d>:\n", users.size());
        users.forEach(System.out::println);
    }

    /*private void testPixApi() {
        efiHelper.consultarListaDeCobrancas();
        CobrancaImediata.Response cobrancaImediata = efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert Gadelha"), 0.25f, 3600);
        if(cobrancaImediata != null) {
            GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);
            System.out.println("\n\nqrCode: " + qrCode);
        }
    }*/

}