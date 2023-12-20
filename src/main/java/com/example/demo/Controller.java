package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("")
    public String index(){
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/change_password")
    public String changePass(){
        return "changepwd";
    }

    @GetMapping("/product")
    public String product(){
        return "product_manage";
    }

    @GetMapping("/profile")
    public String profile(){
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
