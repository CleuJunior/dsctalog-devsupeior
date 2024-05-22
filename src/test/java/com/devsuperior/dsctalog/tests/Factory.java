package com.devsuperior.dsctalog.tests;

import com.devsuperior.dsctalog.dto.ProductDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.entities.Product;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Factory {
    public static Product createProduct() {
        var product = Product.builder()
                .id(1L)
                .name("Phone")
                .description("Good Phone")
                .price(800.0)
                .imgUrl("https://img.com/img.png")
                .date(Instant.parse("2020-07-14T10:00:00Z"))
                .build();

        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO() {
        var product = Factory.createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory() {
        return new Category(2L, "Electronics");
    }

}
