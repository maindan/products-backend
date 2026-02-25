package com.products.products.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialUpdateRequestDTO {

    private String code;
    private String name;
    private BigDecimal stockQuantity;
}