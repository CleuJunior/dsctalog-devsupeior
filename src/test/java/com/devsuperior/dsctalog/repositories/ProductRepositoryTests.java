package com.devsuperior.dsctalog.repositories;

import com.devsuperior.dsctalog.entities.Product;
import com.devsuperior.dsctalog.tests.Factory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
@RequiredArgsConstructor
class ProductRepositoryTests {
    private final ProductRepository productRepository;
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
    void deleteShouldDeleteObjectWhenIdExists(){
        this.productRepository.deleteById(this.existId);
        Optional<Product> result = this.productRepository.findById(this.existId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists(){
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.productRepository.deleteById(this.nonExistingId));
    }

    @Test
    void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = this.productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(this.countTotalProducts + 1, product.getId());
    }

    @Test
    void findByIdShouldReturnNoEmptyOptionalWhenIdExists(){
        Optional<Product> result = this.productRepository.findById(this.existId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void findByIdShouldReturnNoEmptyOptionalWhenIdDoesNotExist(){
        Optional<Product> result = this.productRepository.findById(this.nonExistingId);
        Assertions.assertTrue(result.isEmpty());
    }
}
