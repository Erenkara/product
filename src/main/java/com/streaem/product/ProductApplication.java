package com.streaem.product;

import com.streaem.product.service.DataIngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApplication implements CommandLineRunner {

    private final DataIngestionService dataIngestionService;

    @Autowired
    public ProductApplication(DataIngestionService dataIngestionService) {
        this.dataIngestionService = dataIngestionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dataIngestionService.ingestData();
    }
}
