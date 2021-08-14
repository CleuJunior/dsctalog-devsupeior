package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.entities.Product;
import com.devsuperior.dsctalog.repositories.ProductRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsctalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach()
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 4L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.empty());
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesExists(){
        Assertions.assertThrows(DatabaseException.class, () ->{
            productService.delete(dependentId);
        });
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowEmptyResourceNotFoundExceptionWhenIdDoesExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            productService.delete(nonExistingId);
        });
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() ->{
            productService.delete(existingId);
        });
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

}
