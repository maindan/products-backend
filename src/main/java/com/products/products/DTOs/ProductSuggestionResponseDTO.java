package com.products.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductSuggestionResponseDTO {
    private List<ProductSuggestiontItemDTO> items;
    private BigDecimal totalValue;
}
