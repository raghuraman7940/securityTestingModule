package com.hcl.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;


@SpringBootApplication
public class SecurityTestingModuleApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SecurityTestingModuleApplication.class, args);
//		SpringApplicationBuilder app = new SpringApplicationBuilder(SecurityTestingModuleApplication.class).web(true);
//		app.build().addListeners(new ApplicationPidFileWriter("./bin/shutdown.pid"));
//		app.run();
		
		SpringApplication.run(SecurityTestingModuleApplication.class, args);


	}
}
