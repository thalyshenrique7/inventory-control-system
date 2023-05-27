package com.devthalys.inventorycontrolsystem.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/edit")
    public String editProduct(){
        return "edit_product";
    }
}
