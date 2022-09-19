package com.dt.virtualtubing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Virtual Tubing",
		description = "The virtual tubing microservice models the inner and external tubes of the oil well production column. "
				+ "The tubing is responsible for transporting oil and gas from deep in the well to the surface.",
		version = "1.0.0"), servers = { @Server(url = "http://localhost:8085", description = "Local Docker deployment URL") })

public class VirtualTubingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualTubingApplication.class, args);
	}

}
