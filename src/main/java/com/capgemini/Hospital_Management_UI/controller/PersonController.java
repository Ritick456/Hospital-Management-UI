package com.capgemini.Hospital_Management_UI.controller;






import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {


    @GetMapping("/people")
    public String getPeople(Model model) {
        return "people";
    }

   
}
