package com.ebgr.pagamento_carnes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class PagamentoCarnesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagamentoCarnesApplication.class, args);
	}

}
