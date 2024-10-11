package com.Wojtek03.invoice_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceDto {
    private Long id;
    private Long orderId;
    private LocalDateTime createdAt;
}
