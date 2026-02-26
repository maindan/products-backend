package com.products.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MaterialUpdateRequestDTO {

    private String code;
    private String name;
    private BigDecimal stockQuantity;
}