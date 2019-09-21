package com.charlesluxinger.money.api;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.charlesluxinger.money.api.config.property.MoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(MoneyApiProperty.class)
public class MoneyHoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyHoneyApiApplication.class, args);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	//	@Bean
	//	public WebMvcConfigurer corsConfigurer() {
	//		return new WebMvcConfigurer() {
	//			@Override
	//			public void addCorsMappings(CorsRegistry registry) {
	//				registry.addMapping("/*").allowedOrigins("http://localhost:8000");
	//			}
	//		};
	//	}

}
