package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Objects;


@Controller
public class AccountController {
    @Autowired
    private AccountService sv;
    @GetMapping("")
    public String index(HttpSession session, Model model){
        Account account = (Account) session.getAttribute("loggedAccount");
        if(account != null) {
            model.addAttribute("account", account);
            return "index";
        }

        return "login";

    }

    @GetMapping("/login")
    public String login(HttpSession session, HttpServletResponse res) throws IOException {
        if((Account)session.getAttribute("loggedAccount")!=null){
            res.sendRedirect("");
        }
        return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute Account account, HttpSession session,Model model) throws Exception {

        // Call service xử lý đăng nhập
        Account loggedAccount = sv.findByUsername(account.getUsername());

        if(!Objects.equals(loggedAccount.getPassword(), account.getPassword())) {
            model.addAttribute("error", "Mật khẩu không chính xác");
            return "login";

        }

        else{


        // Lưu user vào session
        session.setAttribute("loggedAccount", loggedAccount);

        return "index";}
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute Account account,Model model)
    {
        // Validate username whether it is exist
        Account findAcc = sv.findByUsername(account.getUsername());
        if (findAcc != null) {
            model.addAttribute("error", "Mật khẩu không chính xác");
            return "register";
        }

        // TODO: handle send gmail
        if (!findAcc.isRegisterStatus())
        {

            return "login";
        }

        sv.addAccount(account);
        model.addAttribute("success", true);

        // Check other errors
        return "index";

    }

    @GetMapping("/change_password")
    public String changePass(){
        return "changePassword";
    }
    @PostMapping("/change_password")
    public String changePw(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session) throws Exception {

        Account account = (Account) session.getAttribute("loggedAccount");
        System.out.println(account.toString());

        sv.changePassword(account, oldPassword, newPassword);

        return "index";
    }

    @GetMapping("/product")
    public String product(){
        return "product_manage";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session,Model model){
        Account account = (Account) session.getAttribute("loggedAccount");
        model.addAttribute("account",account);

        return "profile_manage";
    }

    @GetMapping("/saleManagement")
    public String saleManage(){
        return "sale_manage";
    }

    @GetMapping("/staffManagement")
    public String staffManage(){
        return "staff_manage";
    }

}
