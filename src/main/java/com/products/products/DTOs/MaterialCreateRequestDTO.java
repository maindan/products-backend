package com.products.products.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialCreateRequestDTO {

    private String code;
    private String name;
    private BigDecimal stockQuantity;
}