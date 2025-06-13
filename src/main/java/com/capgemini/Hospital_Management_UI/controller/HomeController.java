package com.capgemini.Hospital_Management_UI.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller

public class HomeController {

    // 👥 View general people page
    @GetMapping("/home")
    public String getPeople(Model model) {
        return "people";
    }

   

}
