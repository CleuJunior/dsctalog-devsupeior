package com.devsuperior.dsctalog.dto;

import com.devsuperior.dsctalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
