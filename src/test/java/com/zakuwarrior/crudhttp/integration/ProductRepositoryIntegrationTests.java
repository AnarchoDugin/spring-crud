package com.zakuwarrior.crudhttp.integration;

import com.zakuwarrior.crudhttp.model.Product;
import com.zakuwarrior.crudhttp.repository.ProductRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class ProductRepositoryIntegrationTests {
    @Autowired
    private ProductRepository productRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.0-alpine")
            .withDatabaseName("product-repo-test")
            .withUsername("application-test-user")
            .withPassword("application-test-password")
            .withInitScript("schema.sql");

    @DynamicPropertySource
    public static void configureDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    public static void containerInit() {
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void containerCleanup() {
        postgreSQLContainer.stop();
    }

    @Test
    void saveAndFindProduct_ShouldWork() {
        Product product = new Product(null, "Laptop", "High-end", 1500.0, 10, "Laptop");

        Product savedProduct = productRepository.save(product);
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals("Laptop", foundProduct.get().getName());
    }

    @Test
    void updateProduct_ShouldChangeData() {
        Product product = new Product(null, "Phone", "Old", 500.0, 5, "Smartphone");
        Product savedProduct = productRepository.save(product);
        savedProduct.setDescription("New");

        Product updatedProduct = productRepository.save(savedProduct);

        assertEquals("New", updatedProduct.getDescription());
    }

    @Test
    void deleteProduct_ShouldRemoveFromDb() {
        Product product = new Product(null, "Tablet", "Desc", 300.0, 8, "Tablet");
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());
        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());

        assertFalse(deletedProduct.isPresent());
    }
}
