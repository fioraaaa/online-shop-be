package com.project.online_shop_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.project.online_shop_be", "lib.minio", "lib.i18n"})
public class OnlineShopBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopBeApplication.class, args);
	}

}
