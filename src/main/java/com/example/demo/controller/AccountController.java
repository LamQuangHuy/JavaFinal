package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.account.Account;
import com.example.demo.model.account.AccountRepository;
import com.example.demo.model.order.Orders;
import com.example.demo.model.order.OrdersRepository;
import com.example.demo.model.order_info.OrderInfo;
import com.example.demo.model.order_info.OrderInfoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
  @Autowired
  private AccountRepository accountRepo;

  @Autowired
  private OrdersRepository orderRepo;

  @Autowired
  private OrderInfoRepository orderInfoRepo;

  public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

  @GetMapping("/profile")
  public String profile(HttpSession session, RedirectAttributes redirAttrs, Model model) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (account.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "You must change your password to access the system.");
      return "redirect:/auth/change-password";
    }
    model.addAttribute("account", account);
    return "profile_manage";
  }

  @PostMapping("/profile")
  public String uploadImage(@RequestParam("avatar") MultipartFile file, HttpSession session) throws IOException {
    StringBuilder fileNames = new StringBuilder();
    Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
    fileNames.append(file.getOriginalFilename());
    Files.write(fileNameAndPath, file.getBytes());

    Account account = (Account) session.getAttribute("loggedInAccount");
    String dynamicPath = "./uploads/" + file.getOriginalFilename();
    account.setAvatar(dynamicPath);
    accountRepo.save(account);
    session.setAttribute("loggedInAccount", account);
    
    return "redirect:/profile";
  }

  @GetMapping("/report")
  public String getReport(@RequestParam(name = "from", required = false) String fromDate,
      @RequestParam(name = "to", required = false) String toDate, HttpSession session, RedirectAttributes redirAttrs, Model model) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (account.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "You must change your password to access the system.");
      return "redirect:/auth/change-password";
    }

    if (fromDate != null) {
      int profit = 0;
      int amtReceived = 0;
      LocalDateTime today = LocalDateTime.now();
      Iterable<Orders> orders = new ArrayList<Orders>();
      List<Account> customers = new ArrayList<Account>();
      List<Integer> numOfProducts = new ArrayList<Integer>();

      if (fromDate.equals("today")) {
        orders = orderRepo.findAllByDate(today);
      } else if (fromDate.equals("yesterday")) {
        LocalDateTime yesterday = today.minus(Period.ofDays(1));
        orders = orderRepo.findAllByDate(yesterday);
      } else if (fromDate.equals("week")) {
        LocalDateTime oneWeekAgo = today.minus(Period.ofDays(7));
        orders = orderRepo.findAllByDateBetween(oneWeekAgo, today);
      } else if (fromDate.equals("month")) {
        LocalDateTime oneMonthAgo = today.minus(Period.ofMonths(1));
        orders = orderRepo.findAllByDateBetween(oneMonthAgo, today);
      } else if (fromDate.equals("quarter")) {
        LocalDateTime threeMonthsAgo = today.minus(Period.ofMonths(3));
        orders = orderRepo.findAllByDateBetween(threeMonthsAgo, today);
      } else if (fromDate.equals("year")) {
        LocalDateTime oneYearAgo = today.minus(Period.ofMonths(12));
        orders = orderRepo.findAllByDateBetween(oneYearAgo, today);
      } else {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
        LocalDateTime from = LocalDateTime.parse(fromDate + " 00:00:00", formatter);        
        LocalDateTime to = LocalDateTime.parse(toDate + " 00:00:00", formatter);
        orders = orderRepo.findAllByDateBetween(from, to);
      }
      for (Orders order : orders) {
          Account customer = accountRepo.findByOrderId(order.getId());
          List<OrderInfo> orderInfos = orderInfoRepo.findAllByOrderId(order.getId());
          numOfProducts.add(orderInfos.size());
          customers.add(customer);
          profit += order.getProfit();
          amtReceived += order.getTotalPrice();
        }
      model.addAttribute("customers", customers);
      model.addAttribute("amtReceived", amtReceived);
      model.addAttribute("orders", orders);
      model.addAttribute("numOfProducts", numOfProducts);
      model.addAttribute("profit", profit);      
      model.addAttribute("role", account.getRole());
    }
    return "report";
  }
}
