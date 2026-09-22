package com.gdp.codetest.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gdp.codetest.entity.Product;
import com.gdp.codetest.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getReturnsAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Alpha", "first", 1.0),
                new Product(2L, "Beta", "second", 2.0));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.Get();

        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    void getByIdReturnsProductWhenPresent() {
        Product product = new Product(1L, "Alpha", "first", 1.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.Get(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Alpha");
    }

    @Test
    void getByIdReturnsNullWhenMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Product result = productService.Get(99L);

        assertThat(result).isNull();
    }

    @Test
    void saveReturnsTrue() {
        Product product = new Product("Gamma", "third", 3.0);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Boolean result = productService.Save(product);

        assertThat(result).isTrue();
        verify(productRepository).save(product);
    }

    @Test
    void deleteReturnsTrueWhenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        Boolean result = productService.Delete(1L);

        assertThat(result).isTrue();
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteReturnsFalseWhenMissing() {
        when(productRepository.existsById(99L)).thenReturn(false);

        Boolean result = productService.Delete(99L);

        assertThat(result).isFalse();
        verify(productRepository, never()).deleteById(any());
    }
}
