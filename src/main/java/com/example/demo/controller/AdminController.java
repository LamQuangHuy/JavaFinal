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
import com.example.demo.model.product.Product;
import com.example.demo.model.product.ProductRepository;
import com.example.demo.utils.MailProvider;
import com.example.demo.utils.TokenProvider;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
  @Autowired
  private AccountRepository accountRepo;
  @Autowired
  private ProductRepository productRepo;
  @Autowired
  private MailProvider mailProvider;
  @Autowired
  private TokenProvider tokenProvider;

  @GetMapping("/admin/employees")
  public String getEmployees(HttpSession session, Model model) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (!account.getRole().equals("admin")) {
      return "error";
    }
    Iterable<Account> list = accountRepo.findAll();
    model.addAttribute("title", "Employees Management");
    model.addAttribute("accounts", list);
    return "admin/employee_manage";
  }

  @PostMapping("/admin/employees")
  public String addNewEmployee(@RequestParam("name") String name, @RequestParam("email") String email,
      RedirectAttributes redirAttrs) {
    boolean isExist = accountRepo.existsByEmail(email);
    if (isExist) {
      redirAttrs.addFlashAttribute("error", "The email is already existed.");
    } else {
      Account account = new Account(name, email);
      account.setRole("user");
      accountRepo.save(account);
      String token = tokenProvider.generateToken(account.getEmail());
      String title = account.getName() + ", Here is your login link.";
      String body = "Welcome to our system, here is your login link. Please, don't send to anyone else. Link: http://localhost:8080/auth/login?token="
          + token;
      mailProvider.sendSimpleEmail(account.getEmail(), title, body);
      redirAttrs.addFlashAttribute("success", "Add employee successfully.");
    }
    return "redirect:/admin/employees";
  }

  @PostMapping("/admin/employees/edit")
  public String editEmployee(@RequestParam("id") int id, @RequestParam("name") String name,
      @RequestParam("email") String email, RedirectAttributes redirAttrs) {
    Account account = accountRepo.findById(id);
    if (account == null) {
      redirAttrs.addFlashAttribute("error", "The employee doesn't exists.");
    } else {
      account.setName(name);
      account.setEmail(email);
      accountRepo.save(account);
      redirAttrs.addFlashAttribute("success", "Update employee successfully.");
    }
    return "redirect:/admin/employees";
  }

  @PostMapping("/admin/employees/lock")
  public String lockEmployee(@RequestParam("id") int id, RedirectAttributes redirAttrs) {
    Account account = accountRepo.findById(id);
    if (account == null) {
      redirAttrs.addFlashAttribute("error", "The employee doesn't exists.");
    } else {
      account.setStatus("locked");
      accountRepo.save(account);
      redirAttrs.addFlashAttribute("success", "Lock employee successfully.");
    }
    return "redirect:/admin/employees";
  }

  @GetMapping("/admin/employees/unlock")
  public String unlockEmployee(@RequestParam("id") int id, HttpSession session, RedirectAttributes redirAttrs) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (!account.getRole().equals("admin")) {
      return "error";
    }
    Account targetAccount = accountRepo.findById(id);
    if (targetAccount == null) {
      redirAttrs.addFlashAttribute("error", "The employee doesn't exists.");
    } else {
      targetAccount.setStatus("active");
      accountRepo.save(targetAccount);
      redirAttrs.addFlashAttribute("success", "Unlock employee successfully.");
    }
    return "redirect:/admin/employees";
  }

  @GetMapping("/admin/employees/resend")
  public String resendLoginLink(@RequestParam("id") int id, HttpSession session, RedirectAttributes redirAttrs) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (!account.getRole().equals("admin")) {
      return "error";
    }
    Account targetAccount = accountRepo.findById(id);
    if (targetAccount == null) {
      redirAttrs.addFlashAttribute("error", "The employee doesn't exists.");
    } else {
      String token = tokenProvider.generateToken(targetAccount.getEmail());
      String title = targetAccount.getName() + ", Here is your login link.";
      String body = "Welcome to our system, here is your login link. Please, don't send to anyone else. Link: http://localhost:8080/auth/login?token="
          + token;
      mailProvider.sendSimpleEmail(targetAccount.getEmail(), title, body);
      redirAttrs.addFlashAttribute("success", "Resend login link successfully.");
    }
    return "redirect:/admin/employees";
  }

  @PostMapping("/admin/employees/delete")
  public String deleteEmployee(@RequestParam("id") int id, RedirectAttributes redirAttrs) {
    boolean isExist = accountRepo.existsById(id);
    if (!isExist) {
      redirAttrs.addFlashAttribute("error", "The employee doesn't exists.");
    } else {
      redirAttrs.addFlashAttribute("success", "Delete employee successfully.");
      accountRepo.deleteById(id);
    }
    return "redirect:/admin/employees";
  }

  @GetMapping("/admin/products")
  public String getProducts(HttpSession session, Model model) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (!account.getRole().equals("admin")) {
      return "error";
    }
    Iterable<Product> list = productRepo.findAll();
    model.addAttribute("products", list);
    return "admin/product_manage";
  }

  @PostMapping("/admin/products")
  public String addNewProduct(@RequestParam("barcode") String barcode, @RequestParam("name") String name,
      @RequestParam("category") String category,
      @RequestParam("import-price") int importPrice, @RequestParam("retail-price") int retailPrice,
      RedirectAttributes redirAttrs) {
    boolean isExist = productRepo.existsByBarcode(barcode);
    if (isExist) {
      redirAttrs.addFlashAttribute("error", "The barcode is already existed.");
    } else {
      Product product = new Product(barcode, name, category, importPrice, retailPrice);
      productRepo.save(product);
      redirAttrs.addFlashAttribute("success", "Add product successfully.");
    }
    return "redirect:/admin/products";
  }

  @PostMapping("/admin/products/edit")
  public String editProduct(@RequestParam("id") int id, @RequestParam("barcode") String barcode,
      @RequestParam("name") String name, @RequestParam("category") String category,
      @RequestParam("import-price") int importPrice, @RequestParam("retail-price") int retailPrice,
      RedirectAttributes redirAttrs) {
    Product product = productRepo.findById(id);
    if (product == null) {
      redirAttrs.addFlashAttribute("error", "The product doesn't exists.");
    } else {
      product.setBarcode(barcode);
      product.setName(name);
      product.setCategory(category);
      product.setImportPrice(importPrice);
      product.setRetailPrice(retailPrice);
      productRepo.save(product);
      redirAttrs.addFlashAttribute("success", "Update product successfully.");
    }
    return "redirect:/admin/products";
  }

  @PostMapping("/admin/products/delete")
  public String deleteProduct(@RequestParam("id") int id, RedirectAttributes redirAttrs) {
    boolean isExist = productRepo.existsById(id);
    if (!isExist) {
      redirAttrs.addFlashAttribute("error", "The product doesn't exists.");
    } else {
      redirAttrs.addFlashAttribute("success", "Delete product successfully.");
      productRepo.deleteById(id);
    }
    return "redirect:/admin/products";
  }
}