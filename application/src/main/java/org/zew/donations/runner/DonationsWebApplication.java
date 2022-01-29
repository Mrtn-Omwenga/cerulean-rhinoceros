package org.zew.donations.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"org.zew.donations"})
public class DonationsWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DonationsWebApplication.class, args);
    }

}
