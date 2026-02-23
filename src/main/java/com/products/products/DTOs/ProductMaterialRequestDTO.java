package com.products.products.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductMaterialRequestDTO {
    private String materialId;
    private BigDecimal quantityRequired;
}
