package com.unitbv.mi.ui.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.unitbv.mi.ui.dto.Product;
import com.unitbv.mi.ui.dto.User;
import com.unitbv.mi.ui.utils.ConnectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.*;

@Controller
public class UiController {
    public UiController() {

    }

    @GetMapping("/addProduct")
    public String addProductPage(Model model) {
        return "addProduct";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        return "checkout";
    }

    @PostMapping("/addProductToDatabase")
    public String addProduct(Product product, BindingResult result, Model model) {
        String url_billing = "http://localhost:8077/addProduct";
        String url_warehouse = "http://localhost:8077/addProduct";
        try {
            Gson gson = new GsonBuilder().create();
            String productJson = gson.toJson(product);
            HashMap<String, String> params = new HashMap<>();
            params.put("productJson", productJson);
            ConnectionUtils.performPostCall(url_warehouse, params, "POST");
            ConnectionUtils.performPostCall(url_billing, params, "POST");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<Product> products = getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/";
        }
        Gson gson = new GsonBuilder().create();
        String url = "http://localhost:8081/register/";
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("userDtoJson", gson.toJson(user));
            String response = ConnectionUtils.performPostCall(url, params, "POST");
            boolean resp = gson.fromJson(response, boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@Valid User user, BindingResult result, Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginUser")
    public String loginUser(@Valid User user, BindingResult result, Model model) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return "/";
        }
        Gson gson = new GsonBuilder().create();
        String url = "http://localhost:8081/login/";
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("userDtoJson", gson.toJson(user));
            String response = ConnectionUtils.performPostCall(url, params, "POST");
            User resp = gson.fromJson(response, User.class);
            if (resp != null) {
                model.addAttribute("loggedUser", resp.getName());
                return "index";
            } else {
                model.addAttribute("loginValidation", "Wrong credentials");
                return "login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("loginValidation", "Bad request");
            return "login";
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        try {
            List<Product> products = getAllProducts();
            model.addAttribute("products", products);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("products", new ArrayList<Product>());
        }
        model.addAttribute("cart", new ArrayList<Product>());
        return "index";
    }

    private List<Product> getAllProducts() {
        String url = "http://localhost:8079/getAllProducts";
        String productsJson = ConnectionUtils.performGetCall(url);
        List<Product> detailedProducts = getDetailsForProducts(productsJson);
        return detailedProducts;
    }

    private List<Product> getDetailsForProducts(String productsJson) {
        String url = "http://localhost:8077/getProductsDetails";
        Type listType = new TypeToken<List<Product>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        List<Product> products = gson.fromJson(productsJson, listType);
        HashMap<String, String> params = new HashMap<>();
        params.put("prodcutsJson", productsJson);
        String responseJson = ConnectionUtils.performPostCall(url, params, "POST");
        List<Product> detailedProducts = gson.fromJson(responseJson, listType);
        for (Product product : products) {
            product.setPrice(detailedProducts.get(detailedProducts.indexOf(product)).getPrice());
        }
        return products;
    }

}