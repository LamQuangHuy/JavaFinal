package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.account.Account;
import com.example.demo.model.account.AccountRepository;
import com.example.demo.utils.TokenProvider;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
  @Autowired
  private AccountRepository repo;
  @Autowired
  private TokenProvider tokenProvider;

  @GetMapping("/auth/login")
  public String login(@RequestParam(name = "token", required = false) String token, HttpSession session,
      RedirectAttributes redirAttrs, Model model) {
    if (token != null) {
      boolean isValid = tokenProvider.validateToken(token);
      if (isValid) {
        String email = tokenProvider.getUserEmailFromJWT(token);
        Account account = repo.findByEmail(email);
        session.setAttribute("loggedInAccount", account);
        redirAttrs.addFlashAttribute("isNew", true);
        redirAttrs.addFlashAttribute("success", "Login successfully. Please set your password.");
        return "redirect:/auth/change-password";
      }
      model.addAttribute("error", "Your login link is expired. Please ask administrator to resend a new link.");
    }
    return "auth/login";
  }

  @PostMapping("/auth/login")
  public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session,
      RedirectAttributes redirAttrs) {
    Account DBaccount = repo.findByUsername(username);

    // Check user exists
    if (DBaccount == null) {
      redirAttrs.addFlashAttribute("error", "Invalid username or password!");
      return "redirect:/auth/login";
    }

    // First time login
    if (DBaccount.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "Please login by clicking on the link in your email.");
      return "redirect:/auth/login";
    }

    // Check password matches
    boolean isMatch = DBaccount.getPassword().equals(password);
    if (!isMatch) {
      redirAttrs.addFlashAttribute("error", "Invalid username or password!");
      return "redirect:/auth/login";
    }
    
    session.setAttribute("loggedInAccount", DBaccount);
    return "redirect:/";
  }

  @GetMapping("/auth/change-password")
  public String changePassword() {
    return "auth/changePassword";
  }

  @PostMapping("/auth/change-password")
  public String handleChangePassword(@RequestParam(name = "old-pass", required = false) String oldPass, @RequestParam("new-pass") String newPass,
      @RequestParam("re-pass") String rePass, HttpSession session, RedirectAttributes redirAttrs) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    boolean isNew = account.getStatus().equals("new");

    // Check confirm password
    if (!rePass.equals(newPass)) {
      redirAttrs.addFlashAttribute("error", "The confirm password must match.");
      redirAttrs.addFlashAttribute("isNew", isNew);
      return "redirect:/auth/change-password";
    }

    // Check old password
    if (!account.getStatus().equals("new")) {
      if (!account.getPassword().equals(oldPass)) {
        redirAttrs.addFlashAttribute("error", "The password is incorrect.");
        return "redirect:/auth/change-password";
      }
    }

    // Update password and status
    account.setPassword(newPass);
    if (isNew) {
      account.setStatus("active");
    }
    repo.save(account);
    return "redirect:/";
  }

  @GetMapping("/auth/logout")
public String logout(HttpSession session) {
    // Invalidate the session
    session.invalidate();
    return "redirect:/auth/login";
}
}


