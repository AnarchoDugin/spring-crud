package com.zakuwarrior.crudhttp.unit;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.zakuwarrior.crudhttp.model.Product;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenNameIsBlank_ShouldFailValidation() {
        Product product = new Product(1L, "", "Description", 100.0, 10, "Electronics");
        var violations = validator.validate(product);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenPriceIsNegative_ShouldFailValidation() {
        Product product = new Product(1L, "Laptop", "Description", -100.0, 10, "Electronics");
        var violations = validator.validate(product);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenQuantityIsNegative_ShouldFailValidation() {
        Product product = new Product(1L, "Laptop", "Description", 100.0, -10, "Electronics");
        var violations = validator.validate(product);
        assertFalse(violations.isEmpty());
    }
}