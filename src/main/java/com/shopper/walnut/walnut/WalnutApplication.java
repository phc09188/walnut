package com.shopper.walnut.walnut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class WalnutApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalnutApplication.class, args);
	}

}
