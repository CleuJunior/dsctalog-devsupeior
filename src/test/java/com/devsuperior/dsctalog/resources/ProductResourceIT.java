package com.devsuperior.dsctalog.resources;


import com.devsuperior.dsctalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach()
    void setup() {
        existingId = 1L;
        nonExistingId = 2333L;
        countTotalProducts = 25L;
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        var productDTO = Factory.createProductDTO();
        var jsonBody = objectMapper.writeValueAsString(productDTO);

        var expectedName = productDTO.getName();
        var expectedDescription = productDTO.getDescription();

        var result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    void updateShouldReturnNotFoundWhenIdNotExists() throws Exception {
        var productDTO = Factory.createProductDTO();
        var jsonBody = objectMapper.writeValueAsString(productDTO);

        var expectedName = productDTO.getName();
        var expectedDescription = productDTO.getDescription();

        var result = mockMvc
                .perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        var result = mockMvc
                .perform(get("/products?page=0&size=12&sort=name,asc").accept(APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }
}
