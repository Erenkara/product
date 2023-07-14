package com.streaem.product.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("data-ingestion")
public class DataIngestionProperties {
    private String productDataUrl;

    public String getProductDataUrl() {
        return productDataUrl;
    }

    public void setProductDataUrl(String productDataUrl) {
        this.productDataUrl = productDataUrl;
    }
}
