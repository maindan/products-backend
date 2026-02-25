package com.products.products.services;

import com.products.products.DTOs.*;
import com.products.products.models.Material;
import com.products.products.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Transactional
    public MaterialResponseDTO create(MaterialCreateRequestDTO request) {

        Material material = new Material();
        material.setCode(request.getCode());
        material.setName(request.getName());
        material.setStockQuantity(request.getStockQuantity());

        materialRepository.save(material);

        return toDTO(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponseDTO> getAll() {
        return materialRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MaterialResponseDTO getById(String id) {

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        return toDTO(material);
    }

    @Transactional
    public MaterialResponseDTO update(String id, MaterialUpdateRequestDTO request) {

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        Optional.ofNullable(request.getCode()).ifPresent(material::setCode);
        Optional.ofNullable(request.getName()).ifPresent(material::setName);
        Optional.ofNullable(request.getStockQuantity()).ifPresent(material::setStockQuantity);

        materialRepository.save(material);

        return toDTO(material);
    }

    @Transactional
    public void delete(String id) {
        if (!materialRepository.existsById(id)) {
            throw new RuntimeException("Material não encontrado");
        }

        materialRepository.deleteById(id);
    }

    private MaterialResponseDTO toDTO(Material material) {
        return new MaterialResponseDTO(
                material.getId(),
                material.getCode(),
                material.getName(),
                material.getStockQuantity()
        );
    }
}