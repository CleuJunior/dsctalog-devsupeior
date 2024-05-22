package com.devsuperior.dsctalog.services;


import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.dto.ProductDTO;
import com.devsuperior.dsctalog.entities.Product;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.repositories.ProductRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(prod -> mapper.map(prod, ProductDTO.class));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        var response = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return mapper.map(response, ProductDTO.class);
    }

    @Transactional
    public ProductDTO insert(ProductDTO request) {
        var response = mapper.map(request, Product.class);
        response = productRepository.save(response);

        return mapper.map(response, ProductDTO.class);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO request) {
        var response = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setDate(request.getDate());
        response.setImgUrl(request.getImgUrl());
        response.setPrice(request.getPrice());
        response.getCategories().clear();

        copyCategoryToEntity(request.getCategories(), response);
        response = productRepository.save(response);

        return mapper.map(response, ProductDTO.class);
    }

    public void delete(Long id) {
        try {
            var product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

            productRepository.delete(product);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyCategoryToEntity(List<CategoryDTO> categoriesDTO, Product product) {
        categoriesDTO.stream()
                .map(catDto -> categoryRepository.getOne(catDto.getId()))
                .forEach(product.getCategories()::add);

    }
}
