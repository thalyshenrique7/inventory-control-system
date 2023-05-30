package com.devthalys.inventorycontrolsystem.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/update_product")
    public String editProduct(){
        return "update_product";
    }
}
