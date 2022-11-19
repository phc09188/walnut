package com.shopper.walnut.walnut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class WalnutApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalnutApplication.class, args);
	}

}
