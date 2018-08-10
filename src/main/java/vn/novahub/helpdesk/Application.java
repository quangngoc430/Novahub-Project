package vn.novahub.helpdesk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import vn.novahub.helpdesk.seeding.ApplicationSeeder;

import java.io.IOException;
import java.text.ParseException;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"vn.novahub.helpdesk"})

@SpringBootApplication
@EnableAsync
public class Application {

	@Autowired
	private ApplicationSeeder applicationSeeder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Profile("dev")
	public String dev() throws IOException, ParseException {
		System.out.println("-------------------------- DEV ENVIRONMENT --------------------------");
		applicationSeeder.generateData();
		return "dev";
	}

	@Bean
	@Profile("prod")
	public String prod() {
		System.out.println("-------------------------- PROD ENVIRONMENT --------------------------");
		return "prod";
	}

	@Bean
	@Profile("test")
	public String test() {
		System.out.println("-------------------------- TEST ENVIRONMENT --------------------------");
		return "test";
	}
}