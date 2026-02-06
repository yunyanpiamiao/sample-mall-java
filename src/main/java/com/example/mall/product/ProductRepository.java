package com.example.mall.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final ConcurrentHashMap<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ProductRepository() {
        // 从JSON文件加载示例数据
        loadProductsFromJson();
    }
    
    private void loadProductsFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("products.json");
            InputStream inputStream = resource.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Product> products = objectMapper.readValue(inputStream, new TypeReference<List<Product>>(){});
                
            for (Product product : products) {
                save(product);
            }
        } catch (IOException e) {
            System.err.println("Failed to load products from JSON: " + e.getMessage());
            // 如果JSON加载失败,使用默认数据
            save(new Product(null, "示例商品 A", "SKU-A", 100, 19900));
            save(new Product(null, "示例商品 B", "SKU-B", 50, 29900));
        }
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        store.put(product.getId(), product);
        return product;
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public List<Product> findByNameContains(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String lower = keyword.toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product product : store.values()) {
            if (product.getName() != null && product.getName().toLowerCase().contains(lower)) {
                result.add(product);
            }
        }
        return result;
    }
}
