package com.unitbv.mi.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages="com.unitbv.mi.ui.dto")
public class TheShopUiApplication {

	public static void main(String[] args) {

		SpringApplication.run(TheShopUiApplication.class, args);
	}

}

