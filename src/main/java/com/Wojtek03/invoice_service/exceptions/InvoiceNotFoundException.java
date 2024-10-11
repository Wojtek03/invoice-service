package com.Wojtek03.invoice_service.exceptions;

import org.springframework.http.HttpStatus;

public class InvoiceNotFoundException extends InvoiceServiceException {
    public InvoiceNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}

