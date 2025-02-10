package com.quaint.qx_bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info=@Info(
				title= "QX's Simulation Bank",
				description="Backend REST APIs for qx-bank",
				version="v1.0",
				contact= @Contact(
						name="Q X",
						email="quaint_xu@yahoo.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Backend REST APIs for qx-bank"
		)
)
public class QxBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(QxBankApplication.class, args);
	}

}
