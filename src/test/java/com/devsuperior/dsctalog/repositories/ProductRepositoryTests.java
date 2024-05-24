package com.devsuperior.dsctalog.repositories;

import com.devsuperior.dsctalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    private long existId;
    private long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() {
        existId = 1L;
        nonExistingId = 31L;
        countTotalProducts = 25L;
    }

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existId);
        var result = productRepository.findById(existId);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists() {
        assertThrows(EmptyResultDataAccessException.class, () -> productRepository.deleteById(nonExistingId));
    }

    @Test
    void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        var product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    void findByIdShouldReturnNoEmptyOptionalWhenIdExists() {
        var result = productRepository.findById(existId);
        assertTrue(result.isPresent());
    }

    @Test
    void findByIdShouldReturnNoEmptyOptionalWhenIdDoesNotExist() {
        var result = productRepository.findById(nonExistingId);
        assertTrue(result.isEmpty());
    }
}
