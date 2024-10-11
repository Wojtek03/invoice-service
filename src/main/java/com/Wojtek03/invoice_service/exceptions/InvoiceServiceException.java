package com.Wojtek03.invoice_service.exceptions;

import org.springframework.http.HttpStatus;

public class InvoiceServiceException extends RuntimeException{
    private final HttpStatus httpStatus;

    public InvoiceServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }
    }