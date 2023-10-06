package me.dio.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.dio.domain.model.Product;
import me.dio.domain.model.respository.ProductRepository;
import me.dio.service.ProductService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;
import static java.util.Optional.ofNullable;


import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Long UNCHANGEABLE_USER_ID = 1L;

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Product create(Product productToCreate) {
        ofNullable(productToCreate).orElseThrow(() -> new BusinessException("Product to create must not be null."));
        this.validateChangeableId(productToCreate.getId(), "created");
        return this.productRepository.save(productToCreate);
    }

    @Transactional
    public Product update(Long id, Product productToUpdate) {
        this.validateChangeableId(id, "updated");
        Product dbProduct = this.findById(id);
        if (!dbProduct.getId().equals(productToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        dbProduct.setName(productToUpdate.getName());
        dbProduct.setPrice(productToUpdate.getPrice());

        return this.productRepository.save(dbProduct);
    }

    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Product dbProduct = this.findById(id);
        this.productRepository.delete(dbProduct);
    }



    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessException("Product with ID %d can not be %s.".formatted(UNCHANGEABLE_USER_ID, operation));
        }
    }
}
