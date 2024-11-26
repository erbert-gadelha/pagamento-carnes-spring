package com.ebgr.pagamento_carnes.controller;

import com.ebgr.pagamento_carnes.controller.dto.PaymentDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentsSummary;
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

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {


    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserRepository userRepository;

    /*@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/pagamentos")
    public String payments(Model model, HttpSession session) {
        HashMap<String, String> userSession = (HashMap<String, String>) session.getAttribute("user");
        if(userSession == null)
            return "redirect:/entrar";
        model.addAttribute("user", new HashMap<>() {{ put("name", userSession.get("name")); }});
        model.addAttribute("title", "Vizualizar pagamentos");

        User user = userRepository.findUserByLogin(userSession.get("name")).orElse(null);
        model.addAttribute("payments", paymentService.createPaymentTable(user));

        int closed = 3;
        model.addAttribute("closed_payments", closed);
        model.addAttribute("remaining_payments", 12 - closed);

        return "payments";
    }*/

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
    public ResponseEntity<PaymentDTO> createPix(@PathVariable int month, @PathVariable int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity
                .ok()
                .body(
                        paymentService.
                                createOrGetPayment((String) authentication.getPrincipal(), month, year)
                );
    }


    /*@GetMapping("api/create-payment/{month}/{year}")
    public String createPix(@PathVariable int month, @PathVariable int year, HttpSession session) {
        HashMap<String, String> userSession = (HashMap<String, String>) session.getAttribute("user");
        if(userSession == null)
            return "redirect:/entrar";

        User user = userRepository.findUserByLogin(userSession.get("name")).orElse(null);
        paymentService.createOrGetPayment(user, month, year);
        return "redirect:/pagamentos";
    }*/

}
