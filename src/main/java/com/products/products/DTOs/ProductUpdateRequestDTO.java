package com.products.products.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductUpdateRequestDTO {
    private String code;
    private String name;
    private BigDecimal price;
    private List<ProductMaterialRequestDTO> materials;
}
