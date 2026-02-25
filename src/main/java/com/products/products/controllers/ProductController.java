package com.products.products.controllers;

import com.products.products.DTOs.ProductCreateRequestDTO;
import com.products.products.DTOs.ProductResponseDTO;
import com.products.products.models.Product;
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
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductCreateRequestDTO request) {
        return ResponseEntity.ok(productService.create(request));
    }
}
