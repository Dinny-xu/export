package com.example.test_pdf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class testController {
    @RequestMapping("/importExcel.html")
    public String index() {
        return "importExcel";
    }
}
