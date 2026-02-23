package com.products.products.services;

import com.products.products.DTOs.ProductCreateRequestDTO;
import com.products.products.DTOs.ProductMaterialRequestDTO;
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
    public Product create(ProductCreateRequestDTO request) {

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

        return productRepository.save(product);
    }
}