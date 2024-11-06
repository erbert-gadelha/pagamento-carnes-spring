package com.ebgr.pagamento_carnes.controller;

import com.ebgr.pagamento_carnes.controller.dto.Login;
import com.ebgr.pagamento_carnes.model.User;
import com.ebgr.pagamento_carnes.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Index {

    @Autowired
    UserService userService;

    @GetMapping("/")
    String index (Model model, HttpSession session) {
        HashMap<String, String> user = (HashMap<String, String>) session.getAttribute("user");
        //model.addAttribute("username", username);

        if(user != null)
            model.addAttribute("user", new HashMap<>() {{ put("name", user.get("name")); }});

        return "index";
    }


    @GetMapping("/entrar")
    public String login(Model model, HttpSession session) {
        model.addAttribute("title", "Entrar");
        return "login";
    }



    @GetMapping("/page")
    public String page(Model model, HttpSession session) {
        return "page";
    }


    @PostMapping("/api/login")
    public String loginApi(@RequestParam String login, @RequestParam String password, HttpSession session) {

        Login.Request request = new Login.Request(login, password);
        System.out.println(request);

        User user = userService.tryToLogin(login, password);
        if(user != null) {
            session.setAttribute("user", new HashMap<>() {{ put("name", user.getName()); }});
            //session.setAttribute("username", request.login());
            return "redirect:/";
        }

        return "redirect:/entrar";
    }

    @GetMapping("/api/logout")
    public String logoutApi(HttpSession session) {
        session.setAttribute("user", null);
        return "redirect:/";
    }
}
