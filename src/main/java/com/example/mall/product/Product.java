package com.example.mall.product;

public class Product {

    private Long id;
    private String name;
    private String sku;
    private Integer stock;
    private Integer price; // 单位：分

    public Product() {
    }

    public Product(Long id, String name, String sku, Integer stock, Integer price) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.stock = stock;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
