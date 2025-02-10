package com.rts.tap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BackendApiApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(BackendApiApplication.class, args);
		
	}

}