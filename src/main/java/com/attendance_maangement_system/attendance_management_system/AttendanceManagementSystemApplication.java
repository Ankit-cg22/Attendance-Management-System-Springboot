package com.attendance_maangement_system.attendance_management_system;

import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableAsync
public class AttendanceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceManagementSystemApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// define the config object
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		// use the config object to configure cors policy for certain urls
		// here we pass "/**" => for all paths
		source.registerCorsConfiguration("/**", config);

		registrationBean.setFilter(new CorsFilter(source));
		registrationBean.setOrder(0);
		// so that this bean is executed first

		return registrationBean;
	}

}
