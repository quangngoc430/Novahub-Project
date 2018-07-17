package vn.novahub.helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "vn.novahub.helpdesk")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}