package com.devsuperior.dsctalog.dto;

import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @Size(min = 5, max = 60, message = "Nome deve ter 5 e 60 caracteres")
    @NotBlank(message = "Campo requirido")
    private String name;

    @NotBlank(message = "Campo requirido")
    private String description;

    @Positive(message = "Preço deve ser um valor positivo")
    private Double price;
    private String imgUrl;

    @PastOrPresent(message = "A data do produto nao pode ser futura")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }
}
