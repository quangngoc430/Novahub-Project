package vn.novahub.helpdesk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import vn.novahub.helpdesk.seeding.ApplicationSeeder;
import vn.novahub.helpdesk.seeding.Seeder;

import java.io.IOException;
import java.text.ParseException;

@Configuration
@SpringBootApplication
@EnableAsync
public class Application {

	private final static Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private ApplicationSeeder applicationSeeder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Profile("dev")
	public String dev() throws IOException, ParseException, ClassNotFoundException {
		logger.info("\n-------------------------- DEV ENVIRONMENT --------------------------");
		applicationSeeder.generateData();
		return "dev";
	}

	@Bean
	@Profile("prod")
	public String prod() {
		logger.info("\n-------------------------- PROD ENVIRONMENT --------------------------");
		return "prod";
	}

	@Bean
	@Profile("test")
	public String test() {
		logger.info("\n-------------------------- TEST ENVIRONMENT --------------------------");
		return "test";
	}
}