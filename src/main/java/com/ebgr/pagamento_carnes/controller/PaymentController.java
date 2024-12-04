package com.ebgr.pagamento_carnes.controller;

import com.ebgr.pagamento_carnes.controller.dto.PaymentDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentMonthDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentsSummary;
import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import com.ebgr.pagamento_carnes.service.PaymentService;
import com.ebgr.pagamento_carnes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {


    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserRepository userRepository;

    @CrossOrigin
    @GetMapping("all")
    public ResponseEntity<Object> getPayments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity
                .ok()
                .body(
                    paymentService.
                            getUserPayments((String) authentication.getPrincipal())
                );
    }
    
    @PostMapping("create/{month}/{year}")
    public ResponseEntity<PaymentMonthDTO> createPix(@PathVariable int month, @PathVariable int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity
                .ok()
                .body(
                        paymentService.
                                createOrGetPayment((String) authentication.getPrincipal(), month, year)
                );
    }

    @PostMapping("webhook")
    public ResponseEntity<String> efiHandShake(@PathVariable int txid) {
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("webhook/{txid}")
    public ResponseEntity<String> efiWebHook(@PathVariable int txid, @RequestBody Map<String, Object> body) {

        System.err.println("webhook foi requisitada.");
        System.err.println("body <" + body.size() + ">");
        for ( String key : body.keySet() )
            System.out.println(key + ":" + body.get(key));

        return ResponseEntity.ok().body(null);
    }


    @Autowired
    EfiHelper efiHelper;

    @GetMapping("webhooks")
    public ResponseEntity<String> getEfiWebHooks() {
        efiHelper.imprimirWebhooks();
        return ResponseEntity.ok().body(null);
    }

}
