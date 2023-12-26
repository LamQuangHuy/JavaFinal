package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.account.Account;

import jakarta.servlet.http.HttpSession;

@Controller
public class index {
  @GetMapping("/")
  public String home(HttpSession session, Model model) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    model.addAttribute("role", account.getRole());
    return "index";
  }

  @GetMapping("/notfound")
  public String error() {
    return "error";
  }
}
