package com.products.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductSuggestiontItemDTO {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalValue;
}
