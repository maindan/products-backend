package com.products.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductWithMaterialsDTO {
    private String id;
    private String code;
    private String name;
    private BigDecimal price;
    private List<ProductMaterialResponseDTO> materials;
}
