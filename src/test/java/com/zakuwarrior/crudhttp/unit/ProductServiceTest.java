package com.zakuwarrior.crudhttp.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zakuwarrior.crudhttp.repository.ProductRepository;
import com.zakuwarrior.crudhttp.model.Product;
import com.zakuwarrior.crudhttp.service.ProductService;
import com.zakuwarrior.crudhttp.model.PageResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        Product product1 = new Product(1L, "Laptop", "High-end gaming laptop", 1500.0, 10, "Laptop");
        Product product2 = new Product(2L, "Phone", "Flagship smartphone", 999.0, 20, "Smartphone");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductsByPage_ShouldReturnPaginatedResults() {
        Product product1 = new Product(1L, "Laptop", "Description", 1500.0, 10, "Laptop");
        when(productRepository.findByPage(0, 5)).thenReturn(Arrays.asList(product1));
        when(productRepository.countAll()).thenReturn(1L);

        PageResponse<Product> response = productService.getProductsByPage(0, 5);

        assertEquals(1, response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(1, response.getTotalPages());
        verify(productRepository, times(1)).findByPage(0, 5);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        Product product = new Product(1L, "Laptop", "Description", 1500.0, 10, "Laptop");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getByProductId(1L);

        assertTrue(foundProduct.isPresent());
        assertEquals("Laptop", foundProduct.get().getName());
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        Product newProduct = new Product(null, "Tablet", "New tablet", 299.0, 15, "Tablet");
        Product savedProduct = new Product(1L, "Tablet", "New tablet", 299.0, 15, "Tablet");
        when(productRepository.save(newProduct)).thenReturn(savedProduct);

        Product result = productService.createProduct(newProduct);

        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}