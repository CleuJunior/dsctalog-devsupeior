package com.devsuperior.dsctalog.tests;

import com.devsuperior.dsctalog.dto.ProductDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct(){
         Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-07-14T10:00:00Z"));
         product.getCategories().add(new Category(2L, "Electronics"));

         return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());

    }


}
