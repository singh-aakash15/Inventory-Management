package com.amakart.order_service.dto;

import com.amakart.order_service.dto.dto.OrderRequestItemDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {
    private Long id;
    private List<OrderRequestItemDto> items;
    private BigDecimal totalPrice;
}
