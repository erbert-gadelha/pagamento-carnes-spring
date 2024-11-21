package com.ebgr.pagamento_carnes.controller;

import com.ebgr.pagamento_carnes.controller.dto.PaymentsSummary;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.model.Payment;
import com.ebgr.pagamento_carnes.model.User;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import com.ebgr.pagamento_carnes.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
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
    @GetMapping("api/payments")
    public PaymentsSummary getPayments(/*HttpSession session*/) {
        //HashMap<String, String> userSession = (HashMap<String, String>) session.getAttribute("user");
        User user = userRepository.findUserByLogin("erbert").orElse(null);

        PaymentsSummary paymentsSummary = paymentService.userPaymentsSummary(user);

        return paymentsSummary;
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
