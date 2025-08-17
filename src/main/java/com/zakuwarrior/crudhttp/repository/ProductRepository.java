package com.zakuwarrior.crudhttp.repository;

import com.zakuwarrior.crudhttp.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findByPage(int offset, int limit);
    long countAll();
    Optional<Product> findById(long id);
    Product save(Product product);
    void deleteById(long id);
}
