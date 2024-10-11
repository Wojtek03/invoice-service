package com.Wojtek03.invoice_service.exceptions;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends InvoiceServiceException{
    public OrderNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
