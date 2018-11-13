package com.iot.project.cartrackingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CarTrackingApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CarTrackingApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		// Adding CORS(Cross Origin Resource Sharing) configuration for the required
		// client.
		registry.addMapping("/**").allowedOrigins("http://mocker.ennate.academy/")
				.allowedMethods("PUT", "DELETE", "POST", "GET").allowedHeaders("*").allowedOrigins("*")
				.exposedHeaders("Access-Control-Allow-Origin:*");
	}
}
