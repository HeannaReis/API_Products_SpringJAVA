package me.dio.controller.dto;

import me.dio.domain.model.Product;

public record ProductDto(
            Long id,
            String name,
            Double price){

        public ProductDto(Product model) {
            this(
                    model.getId(),
                    model.getName(),
                    model.getPrice()
            );
        }

        public Product toModel() {
            Product model = new Product();
            model.setId(this.id);
            model.setName(this.name);
            model.setPrice(this.price);
            return model;
        }

    }
