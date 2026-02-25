package com.products.products.services;

import com.products.products.DTOs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.products.products.models.*;
import com.products.products.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public ProductResponseDTO getById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

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

    public List<ProductWithMaterialsResponseDTO> getAllProductsWithMaterials() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductWithMaterialsResponseDTO(
                        product.getId(),
                        product.getCode(),
                        product.getName(),
                        product.getPrice(),
                        product.getMaterials()
                                .stream()
                                .map(pm -> new ProductMaterialResponseDTO(
                                        pm.getId(),
                                        pm.getMaterial().getId(),
                                        pm.getMaterial().getName(),
                                        pm.getQuantityRequired()
                                ))
                                .toList()
                ))
                .toList();
    }

    @Transactional
    public ProductResponseDTO update(String productId, ProductCreateRequestDTO request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Optional.ofNullable(request.getCode()).ifPresent(product::setCode);
        Optional.ofNullable(request.getName()).ifPresent(product::setName);
        Optional.ofNullable(request.getPrice()).ifPresent(product::setPrice);

        if (request.getMaterials() != null) {

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
        }

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

    @Transactional(readOnly = true)
    public ProductSuggestionResponseDTO suggestionProduction() {

        List<Product> products = productRepository.findAll()
                .stream()
                .sorted((first, second) -> second.getPrice().compareTo(first.getPrice()))
                .toList();

        List<Material> materials = materialRepository.findAll();

        Map<String, BigDecimal> stockMap = materials.stream()
                .collect(Collectors.toMap(
                        Material::getId,
                        Material::getStockQuantity
                ));

        List<ProductSuggestiontItemDTO> suggestions = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Product product : products) {

            int maxProducible = Integer.MAX_VALUE;

            for (ProductMaterial pm : product.getMaterials()) {

                BigDecimal available = stockMap.get(pm.getMaterial().getId());
                BigDecimal required = pm.getQuantityRequired();

                if (available == null || required.compareTo(BigDecimal.ZERO) <= 0) {
                    maxProducible = 0;
                    break;
                }

                int possible = available
                        .divide(required, 0, RoundingMode.DOWN)
                        .intValue();

                maxProducible = Math.min(maxProducible, possible);
            }

            if (maxProducible > 0 && maxProducible != Integer.MAX_VALUE) {

                for (ProductMaterial pm : product.getMaterials()) {

                    String materialId = pm.getMaterial().getId();

                    BigDecimal used = pm.getQuantityRequired()
                            .multiply(BigDecimal.valueOf(maxProducible));

                    stockMap.put(materialId,
                            stockMap.get(materialId).subtract(used));
                }

                BigDecimal productTotal = product.getPrice()
                        .multiply(BigDecimal.valueOf(maxProducible));

                totalPrice = totalPrice.add(productTotal);

                suggestions.add(new ProductSuggestiontItemDTO(
                        product.getId(),
                        product.getName(),
                        maxProducible,
                        productTotal
                ));
            }
        }

        return new ProductSuggestionResponseDTO(
                suggestions,
                totalPrice
        );
    }
}