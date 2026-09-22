package com.gdp.codetest.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdp.codetest.entity.Product;
import com.gdp.codetest.repository.ProductRepository;
import com.gdp.codetest.services.generic.GenericServices;

@Service
public class ProductService implements GenericServices<Product, Long> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> Get() {
        return productRepository.findAll();
    }

    @Override
    public Product Get(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean Save(Product model) {
        productRepository.save(model);
        return Boolean.TRUE;
    }

    @Override
    public Boolean Delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
