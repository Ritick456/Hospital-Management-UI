package com.capgemini.Hospital_Management_UI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HospitalManagementUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementUiApplication.class, args);
	}

}
