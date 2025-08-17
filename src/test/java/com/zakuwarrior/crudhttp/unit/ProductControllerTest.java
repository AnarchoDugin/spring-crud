package com.zakuwarrior.crudhttp.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zakuwarrior.crudhttp.model.Product;
import com.zakuwarrior.crudhttp.service.ProductService;
import com.zakuwarrior.crudhttp.controller.ProductController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void getAllProducts_ShouldReturn200() {
        Product product = new Product(1L, "Laptop", "Description", 1500.0, 10, "Laptop");
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product));

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturn200() {
        Product product = new Product(1L, "Laptop", "Description", 1500.0, 10, "Laptop");
        when(productService.getByProductId(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Laptop", response.getBody().getName());
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldReturn404() {
        when(productService.getByProductId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createProduct_ShouldReturn200() {
        Product newProduct = new Product(null, "Tablet", "Description", 299.0, 15, "Tablet");
        Product savedProduct = new Product(1L, "Tablet", "Description", 299.0, 15, "Tablet");
        when(productService.createProduct(newProduct)).thenReturn(savedProduct);

        ResponseEntity<Product> response = productController.createProduct(newProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void deleteProduct_ShouldReturn204() {
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(1L);
    }
}