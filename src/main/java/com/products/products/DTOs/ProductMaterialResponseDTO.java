package com.products.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductMaterialResponseDTO {
    private String id;
    private String materialId;
    private String materialName;
    private BigDecimal quantityRequired;
}
