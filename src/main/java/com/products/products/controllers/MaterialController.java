package com.products.products.controllers;

import com.products.products.DTOs.*;
import com.products.products.services.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> create(
            @RequestBody MaterialCreateRequestDTO request) {

        return ResponseEntity.ok(materialService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDTO>> getAll() {
        return ResponseEntity.ok(materialService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> getById(
            @PathVariable String id) {

        return ResponseEntity.ok(materialService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> update(
            @PathVariable String id,
            @RequestBody MaterialUpdateRequestDTO request) {

        return ResponseEntity.ok(materialService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}