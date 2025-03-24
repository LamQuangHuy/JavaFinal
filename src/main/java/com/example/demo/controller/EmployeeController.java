package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.account.Account;
import com.example.demo.model.account.AccountRepository;
import com.example.demo.model.order.Orders;
import com.example.demo.model.order.OrdersRepository;
import com.example.demo.model.order_info.OrderInfo;
import com.example.demo.model.order_info.OrderInfoRepository;
import com.example.demo.model.product.Product;
import com.example.demo.model.product.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {
  @Autowired
  private ProductRepository productRepo;

  @Autowired
  private AccountRepository accountRepo;

  @Autowired
  private OrdersRepository orderRepo;

  @Autowired
  private OrderInfoRepository orderInfoRepo;

  @GetMapping("/sale")
  public String sale(HttpSession session, RedirectAttributes redirAttrs) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (account.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "You must change your password to access the system.");
      return "redirect:/auth/change-password";
    }
    return "sale/sale_manage";
  }

  @GetMapping("/checkout")
  public String checkout(HttpSession session, RedirectAttributes redirAttrs) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (account.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "You must change your password to access the system.");
      return "redirect:/auth/change-password";
    }
    return "sale/checkout";
  }

  @GetMapping("/order/{id}/invoice")
  public String getOrderInvoice(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirAttrs,
      Model model) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (account.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "You must change your password to access the system.");
      return "redirect:/auth/change-password";
    }
    Orders order = orderRepo.findById(id);
    Account customer = accountRepo.findById(order.getCustomerId());
    Iterable<Product> products = orderInfoRepo.getProductsByOrderId(id);
    Iterable<OrderInfo> orderInfos = orderInfoRepo.findAllByOrderId(id);

    model.addAttribute("order", order);
    model.addAttribute("customer", customer);
    model.addAttribute("products", products);
    model.addAttribute("orderInfos", orderInfos);
    return "sale/invoice";
  }

  @RequestMapping(path = "/get-all-products", method = RequestMethod.GET)
  public ResponseEntity<Iterable<Product>> getProducts() {
    Iterable<Product> list = productRepo.findAll();
    return new ResponseEntity<Iterable<Product>>(list, HttpStatus.OK);
  }

  @RequestMapping(path = "/customer/{phone}", method = RequestMethod.GET)
  public ResponseEntity<Account> getCustomerByPhone(@PathVariable("phone") String phone) {
    Account account = accountRepo.findCustomerByPhone(phone);
    return new ResponseEntity<Account>(account, HttpStatus.OK);
  }

  @RequestMapping(path = "/customer", method = RequestMethod.POST)
  public ResponseEntity<?> createCustomer(@RequestParam("name") String name, @RequestParam("phone") String phone,
      @RequestParam("address") String address) {
    boolean isExist = accountRepo.existsByPhone(phone);
    if (isExist) {
      return new ResponseEntity<>(null, HttpStatus.OK);
    }
    Account account = new Account();
    account.setName(name);
    account.setPhone(phone);
    account.setAddress(address);
    account.setRole("customer");
    accountRepo.save(account);
    return new ResponseEntity<Account>(account, HttpStatus.OK);
  }

  @RequestMapping(path = "/customer/{id}/orders", method = RequestMethod.GET)
  public ResponseEntity<?> getOrderByCustomerId(@PathVariable("id") int id) {
    Iterable<Orders> orders = orderRepo.findByCustomerId(id);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @RequestMapping(path = "/order", method = RequestMethod.POST)
  public ResponseEntity<?> createOrder(@RequestParam("productIds") String productIds,
      @RequestParam("productQuantities") String productQuantities, @RequestParam("productPrices") String productPrices,
      @RequestParam("customerId") int customerId,
      @RequestParam("totalPrice") int totalPrice) {

    int profit = 0;

    Orders order = new Orders();
    order.setCustomerId(customerId);
    order.setTotalPrice(totalPrice);
    orderRepo.save(order);

    String[] productIdSplit = productIds.split(",");
    String[] productPriceSplit = productPrices.split(",");
    String[] productQuanSplit = productQuantities.split(",");
    for (int i = 0; i < productIdSplit.length; i++) {
      int productId = Integer.parseInt(productIdSplit[i]);
      int quantity = Integer.parseInt(productQuanSplit[i]);
      int price = Integer.parseInt(productPriceSplit[i]);

      // Calculate the profit
      Product product = productRepo.findById(productId);
      profit += (product.getRetailPrice() - product.getImportPrice()) * quantity;

      OrderInfo orderInfo = new OrderInfo(productId, order.getId(), customerId, quantity, price);
      orderInfoRepo.save(orderInfo);
    }

    order.setProfit(profit);
    orderRepo.save(order);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  @GetMapping("/products")
  public String viewProducts(HttpSession session, Model model, RedirectAttributes redirAttrs) {
    Account account = (Account) session.getAttribute("loggedInAccount");
    if (account == null) {
      return "redirect:/auth/login";
    }
    if (account.getRole().equals("admin")) {
      return "redirect:/admin/products";
    }
    if (account.getStatus().equals("new")) {
      redirAttrs.addFlashAttribute("error", "You must change your password to access the system.");
      return "redirect:/auth/change-password";
    }
    Iterable<Product> list = productRepo.findAll();
    model.addAttribute("products", list);
    return "product_manage";
  }
}
