package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@MapperScan("com.example.web.dao")
@SpringBootApplication
@Slf4j
public class TestPdfApplication {

    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext application = SpringApplication.run(TestPdfApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application Poney Gzxf Portal is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + "/" + "\n\t" +
                "External: \thttp://" + ip + ":" + port  + "/" + "\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port  + "/" + "swagger-ui.html\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port  + "/" +"doc.html\n" +
                "----------------------------------------------------------");
    }

}
