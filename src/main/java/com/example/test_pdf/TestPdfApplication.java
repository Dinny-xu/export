package com.example.test_pdf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.test_pdf.dao")
@SpringBootApplication
public class TestPdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestPdfApplication.class, args);
    }

}
