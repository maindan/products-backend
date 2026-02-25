package com.products.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductMaterialResponseDTO {
    private String id;
    private String materialId;
    private String materialName;
    private Integer quantityRequired;
}
