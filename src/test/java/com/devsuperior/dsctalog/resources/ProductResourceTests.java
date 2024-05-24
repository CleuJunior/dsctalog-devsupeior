package com.devsuperior.dsctalog.resources;

import com.devsuperior.dsctalog.config.AppConfig;
import com.devsuperior.dsctalog.dto.ProductDTO;
import com.devsuperior.dsctalog.services.ProductService;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsctalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(ProductResource.class)
@SpringBootTest(classes = { AppConfig.class,  ObjectMapper.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductResourceTests {

    @Mock
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach()
    void setup() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(productService.findAllPaged(any())).thenReturn(page);
        when(productService.findById(existingId)).thenReturn(productDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(productService.insert(any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        when(productService.update(eq(existingId), any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(productService).delete(existingId);

        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);

    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        var result = mockMvc
                .perform(delete("/products/{id}", existingId)
                        .accept(APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        var result = mockMvc.perform(delete("/products/{id}", nonExistingId).accept(APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    void insertShouldReturnProductDTOCreated() throws Exception {
        var result = mockMvc.perform(post("/products")
                .accept(APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }


    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        var jsonBody = objectMapper.writeValueAsString(productDTO);

        var result = mockMvc
                .perform(put("/products", existingId)
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void updateShouldReturnNotFoundWhenIdNotExists() throws Exception {
        var jsonBody = objectMapper.writeValueAsString(productDTO);

        var result = mockMvc
                .perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    void findByIdShouldReturnProductWhenIdExists() throws Exception {
        var result = mockMvc
                .perform(get("/products/{id}", existingId)
                        .accept(APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists() throws Exception {
        var result = mockMvc
                .perform(get("/products/{id}", nonExistingId)
                        .accept(APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        var result = mockMvc
                .perform(get("/products")
                        .accept(APPLICATION_JSON));

        result.andExpect(status().isOk());
    }
}
