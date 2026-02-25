package com.products.products.controllers;

import com.products.products.DTOs.*;
import com.products.products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/with-materials")
    public ResponseEntity<List<ProductWithMaterialsResponseDTO>> getAllProductsWithMaterials() {
        return ResponseEntity.ok(productService.getAllProductsWithMaterials());
    }


    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(
            @RequestBody ProductCreateRequestDTO request) {

        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable String id,
            @RequestBody ProductCreateRequestDTO request) {

        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/suggestion")
    public ResponseEntity<ProductSuggestionResponseDTO> suggestProduction() {

        return ResponseEntity.ok(productService.suggestionProduction());
    }
}