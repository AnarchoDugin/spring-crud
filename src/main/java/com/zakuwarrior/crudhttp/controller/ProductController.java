package com.zakuwarrior.crudhttp.controller;

import com.zakuwarrior.crudhttp.model.PageResponse;
import com.zakuwarrior.crudhttp.model.Product;
import com.zakuwarrior.crudhttp.service.ProductService;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Product API", description = "Basic CRUD API for managing products in a database.")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Retrieve all products",
            description = "Fetches a list of all available products in the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the list of products"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request")
            }
    )
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Served a request to get all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(
            summary = "Retrieve paginated products",
            description = "Fetches a paginated list of products with customizable page size.",
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Zero-based page index (0..n)",
                            example = "0",
                            required = false),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            example = "5",
                            required = false)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the paginated list of products"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid pagination parameters provided"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request")
            }
    )
    @GetMapping("/page")
    public ResponseEntity<PageResponse<Product>> getProductsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("Served a request to get products page {} with size {}", page, size);
        return ResponseEntity.ok(productService.getProductsByPage(page, size));
    }

    @Operation(
            summary = "Retrieve product by ID",
            description = "Fetches a single product by its unique ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Unique identifier of the product",
                            example = "1",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found and returned successfully"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product with the specified ID was not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("Served a request to get a product with id: {}", id);
        Optional<Product> optionalProduct = productService.getByProductId(id);
        return optionalProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new product",
            description = "Adds a new product to the database. The request body must contain valid product data.",
            parameters = {
                    @Parameter(
                            name = "product",
                            description = "Product object that needs to be created",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid product data provided"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request")
            }
    )
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody final Product product) {
        log.info("Served a request to create a product: {}", product);
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an existing product",
            description = "Updates the details of an existing product identified by its ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Unique identifier of the product to be updated",
                            example = "1",
                            required = true),
                    @Parameter(
                            name = "product",
                            description = "Updated product object",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid product data provided"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product with the specified ID was not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody final Product product) {
     log.info("Served a request to update a product with id {}: {}", id, product);
     return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @Operation(
            summary = "Delete a product",
            description = "Removes a product from the database by its ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Unique identifier of the product to be deleted",
                            example = "1",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product deleted successfully"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product with the specified ID was not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Served a request to delete a product with id : {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
