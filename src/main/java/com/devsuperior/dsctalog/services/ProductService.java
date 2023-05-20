package com.devsuperior.dsctalog.services;


import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.dto.ProductDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.entities.Product;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.repositories.ProductRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return this.productRepository.findAll(pageable)
                .map(prod -> this.mapper.map(prod, ProductDTO.class));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
       Optional<Product> optionalProduct = this.productRepository.findById(id);
       Product response = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

       return this.mapper.map(response, ProductDTO.class);
    }

    @Transactional
    public ProductDTO insert(ProductDTO request) {
        Product response = this.mapper.map(request, Product.class);
        response = this.productRepository.save(response);

        return this.mapper.map(response, ProductDTO.class);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO request) {
        try {
            Product response = this.productRepository.getOne(id);

            response.setName(request.getName());
            response.setDescription(request.getDescription());
            response.setDate(request.getDate());
            response.setImgUrl(request.getImgUrl());
            response.setPrice(request.getPrice());
            response.getCategories().clear();

            this.copyCategoryToEntity(request.getCategories(), response);
            response = this.productRepository.save(response);

            return this.mapper.map(response, ProductDTO.class);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            this.productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);

        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyCategoryToEntity(List<CategoryDTO> categoriesDTO, Product product) {
        categoriesDTO.stream()
                .map(catDto -> this.categoryRepository.getOne(catDto.getId()))
                .forEach(product.getCategories()::add);

    }
}
