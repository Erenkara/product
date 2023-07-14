package com.streaem.product.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Version;

@Getter
@Setter
@Accessors(chain = true)
public class Product {
    private Long id;
    private String name;
    private String category;
    private double price;
    private String description;
    private int stock;
    @Version
    private int version;
}

