package tz.co.nezatech.dev.surveysubmission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import tz.co.nezatech.dev.surveysubmission.storage.StorageProperties;



@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {
	static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		LOG.info("Successfully started");

		// @EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
	}
}
