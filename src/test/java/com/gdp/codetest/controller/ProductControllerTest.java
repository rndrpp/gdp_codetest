package com.gdp.codetest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdp.codetest.entity.Product;
import com.gdp.codetest.services.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void getAllReturnsList() throws Exception {
        when(productService.Get()).thenReturn(Arrays.asList(
                new Product(1L, "Alpha", "first", 1.0),
                new Product(2L, "Beta", "second", 2.0)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alpha"));
    }

    @Test
    void getByIdReturnsProduct() throws Exception {
        when(productService.Get(1L)).thenReturn(new Product(1L, "Alpha", "first", 1.0));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alpha"));
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        when(productService.Get(99L)).thenReturn(null);

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createReturns201() throws Exception {
        Product product = new Product("Gamma", "third", 3.0);
        when(productService.Save(any(Product.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Gamma"));
    }

    @Test
    void updateReturns200WhenExists() throws Exception {
        Product existing = new Product(1L, "Alpha", "first", 1.0);
        when(productService.Get(1L)).thenReturn(existing);
        when(productService.Save(any(Product.class))).thenReturn(Boolean.TRUE);

        Product update = new Product("Alpha Updated", "changed", 5.0);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alpha Updated"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateReturns404WhenMissing() throws Exception {
        when(productService.Get(99L)).thenReturn(null);

        Product update = new Product("Nope", "missing", 0.0);

        mockMvc.perform(put("/api/products/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns200WhenDeleted() throws Exception {
        when(productService.Delete(1L)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReturns404WhenMissing() throws Exception {
        when(productService.Delete(eq(99L))).thenReturn(Boolean.FALSE);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }
}
