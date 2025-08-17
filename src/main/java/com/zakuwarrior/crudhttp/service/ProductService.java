package com.zakuwarrior.crudhttp.service;

import com.zakuwarrior.crudhttp.repository.ProductRepository;
import com.zakuwarrior.crudhttp.model.Product;
import com.zakuwarrior.crudhttp.model.PageResponse;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll();
    }

    public PageResponse<Product> getProductsByPage(int page, int size) {
        log.debug("Fetching products for page {} with size {}", page, size);
        int offset = page * size;
        List<Product> products = productRepository.findByPage(offset, size);
        long totalItems = productRepository.countAll();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return new PageResponse<>(products, page, totalPages, totalItems, size);
    }

    public Optional<Product> getByProductId(Long id) {
        log.debug("Fetching product with id: {}", id);
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        log.debug("Creating new product: {}", product);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        log.debug("Updating product with id {}: {}", id, product);
        product.setId(id);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.debug("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
}
