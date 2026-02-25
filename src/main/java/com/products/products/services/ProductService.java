package com.products.products.services;

import com.products.products.DTOs.ProductCreateRequestDTO;
import com.products.products.DTOs.ProductMaterialRequestDTO;
import com.products.products.DTOs.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.products.products.models.*;
import com.products.products.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public ProductResponseDTO create(ProductCreateRequestDTO request) {

        if (request.getMaterials() == null || request.getMaterials().isEmpty()) {
            throw new RuntimeException("Produto deve possuir pelo menos um insumo.");
        }

        Product product = new Product();
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        List<ProductMaterial> productMaterials = new ArrayList<>();

        for (ProductMaterialRequestDTO item : request.getMaterials()) {

            Material material = materialRepository.findById(item.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

            ProductMaterial pm = new ProductMaterial();
            pm.setProduct(product);
            pm.setMaterial(material);
            pm.setQuantityRequired(item.getQuantityRequired());

            productMaterials.add(pm);
        }

        product.setMaterials(productMaterials);
        productRepository.save(product);

        return new ProductResponseDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPrice()
                );
    }

    public List<ProductResponseDTO> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getCode(),
                        product.getName(),
                        product.getPrice()
                ))
                .toList();
    }

    @Transactional
    public ProductResponseDTO update(String productId, ProductCreateRequestDTO request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        product.getMaterials().clear();

        List<ProductMaterial> newMaterials = new ArrayList<>();

        for (ProductMaterialRequestDTO item : request.getMaterials()) {

            Material material = materialRepository.findById(item.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

            ProductMaterial productMaterial = new ProductMaterial();
            productMaterial.setProduct(product);
            productMaterial.setMaterial(material);
            productMaterial.setQuantityRequired(item.getQuantityRequired());

            newMaterials.add(productMaterial);
        }

        product.setMaterials(newMaterials);

        productRepository.save(product);

        return new ProductResponseDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPrice()
        );
    }

    @Transactional
    public void delete(String productId) {

        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Produto não encontrado");
        }

        productRepository.deleteById(productId);
    }
}