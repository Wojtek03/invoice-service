package com.Wojtek03.invoice_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OrderEventDto {
        private Long orderId;
        private String status;
    }

