package vn.novahub.helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@SpringBootApplication(scanBasePackages = "vn.novahub.helpdesk")
public class Application {

	public static void main(String[] args) {
		System.out.println("/api/users/me".matches(""));
		SpringApplication.run(Application.class, args);


	}

}