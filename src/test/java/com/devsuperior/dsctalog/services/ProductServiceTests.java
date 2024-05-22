package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.ProductDTO;
import com.devsuperior.dsctalog.entities.Product;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.repositories.ProductRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import static com.devsuperior.dsctalog.tests.Factory.createProduct;
import static com.devsuperior.dsctalog.tests.Factory.createProductDTO;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;

@ExtendWith(SpringExtension.class)
class ProductServiceTests {

    @Mock
    private ModelMapper mapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach()
    void setup() {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 4L;
        product = createProduct();
        productDTO = createProductDTO();
        page = new PageImpl<>(singletonList(product));
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() {
        when(productRepository.findById(existingId)).thenReturn(of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        var result = productService.update(existingId, productDTO);

        assertNotNull(result);
        verify(productRepository).findById(existingId);
        verify(productRepository).save(product);
        verify(mapper).map(product, ProductDTO.class);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdExists() {
        assertThrows(ResourceNotFoundException.class, () -> productService.update(nonExistingId, productDTO));

        verify(productRepository).findById(nonExistingId);
        verify(productRepository, times(0)).save(product);
        verify(mapper, times(0)).map(product, ProductDTO.class);
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() {
        when(productRepository.findById(existingId)).thenReturn(of(product));
        when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        var result = productService.findById(existingId);

        assertNotNull(result);
        verify(productRepository).findById(existingId);
        verify(mapper).map(product, ProductDTO.class);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdExists() {
        when(productRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingId));

        verify(productRepository).findById(nonExistingId);
        verify(mapper, times(0)).map(product, ProductDTO.class);
    }

    @Test
    void findAllPagedShouldReturnPage() {
        when(productRepository.findAll(of(0, 10))).thenReturn(page);
        when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        var pageable = of(0, 10);
        var result = productService.findAllPaged(pageable);

        assertNotNull(result);
        verify(productRepository).findAll(pageable);
        verify(mapper).map(product, ProductDTO.class);
    }

    @Test
    void shouldInsertProductAndReturnProdctDto() {
        when(mapper.map(productDTO, Product.class)).thenReturn(product);
        when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        when(productRepository.save(product)).thenReturn(product);

        var result = productService.insert(productDTO);

        assertNotNull(result);
        verify(mapper).map(productDTO, Product.class);
        verify(productRepository).save(product);
        verify(mapper).map(product, ProductDTO.class);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenIdDoesExists() {
        when(productRepository.findById(dependentId)).thenReturn(of(product));
        doThrow(DataIntegrityViolationException.class).when(productRepository).delete(product);

        assertThrows(DatabaseException.class, () -> productService.delete(dependentId));

        verify(productRepository).findById(dependentId);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteShouldThrowEmptyResourceNotFoundExceptionWhenIdDoesExists() {
        when(productRepository.findById(nonExistingId)).thenReturn(empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));

        verify(productRepository).findById(nonExistingId);
        verify(productRepository, times(0)).delete(product);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        when(productRepository.findById(existingId)).thenReturn(of(product));

        assertDoesNotThrow(() -> productService.delete(existingId));

        verify(productRepository).findById(existingId);
        verify(productRepository).delete(product);
    }
}
