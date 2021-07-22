package com.devsuperior.dsctalog.resources;

import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll()
    {
        List<CategoryDTO> list = categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findByID(@PathVariable Long id)
    {
        CategoryDTO dto = categoryService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

}
