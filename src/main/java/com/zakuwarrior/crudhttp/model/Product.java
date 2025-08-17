package com.zakuwarrior.crudhttp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Product Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Schema(description = "Unique product id", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "iPhone 12")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Schema(description = "Product description", example = "Apple iPhone 12")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Schema(description = "Product price", example = "159.99")
    @Positive(message = "Price must be positive")
    private double price;

    @Schema(description = "Product quantity", example = "10")
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @Schema(description = "Product category", example = "Smartphone")
    @NotBlank(message = "Category cannot be blank")
    private String category;
}
