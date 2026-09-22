package com.gdp.codetest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gdp.codetest.entity.Product;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void savesAndRetrievesProduct() {
        Product product = new Product("Widget", "A useful widget", 9.99);

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();

        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Widget");
        assertThat(found.get().getDescription()).isEqualTo("A useful widget");
        assertThat(found.get().getPrice()).isEqualTo(9.99);
    }

    @Test
    void findAllReturnsSavedProducts() {
        productRepository.save(new Product("Alpha", "first", 1.0));
        productRepository.save(new Product("Beta", "second", 2.0));

        List<Product> all = productRepository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(Product::getName).containsExactlyInAnyOrder("Alpha", "Beta");
    }

    @Test
    void deletesProduct() {
        Product saved = productRepository.save(new Product("Gamma", "third", 3.0));
        Long id = saved.getId();

        productRepository.deleteById(id);

        assertThat(productRepository.existsById(id)).isFalse();
    }
}
