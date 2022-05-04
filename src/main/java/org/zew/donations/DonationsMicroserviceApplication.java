package org.zew.donations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DonationsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationsMicroserviceApplication.class, args);
	}

}
