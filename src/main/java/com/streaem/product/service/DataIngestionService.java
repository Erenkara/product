package com.streaem.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaem.product.configuration.DataIngestionProperties;
import com.streaem.product.model.Product;
import com.streaem.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class DataIngestionService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final DataIngestionProperties dataIngestionProperties;

    public DataIngestionService(RestTemplate restTemplate,
                                ObjectMapper objectMapper,
                                ProductRepository productRepository,
                                DataIngestionProperties dataIngestionProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.dataIngestionProperties = dataIngestionProperties;
    }

    public void ingestData() throws IOException {
        String jsonFeed = restTemplate.getForObject(dataIngestionProperties.getProductDataUrl(), String.class);
        List<Product> products = objectMapper.readValue(jsonFeed, new TypeReference<>() {
        });

        productRepository.saveAll(products);
    }
}
