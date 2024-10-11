package com.Wojtek03.invoice_service.service;


import com.Wojtek03.invoice_service.dto.InvoiceDto;
import com.Wojtek03.invoice_service.exceptions.InvoiceNotFoundException;
import com.Wojtek03.invoice_service.exceptions.OrderNotFoundException;
import com.Wojtek03.invoice_service.mapper.InvoiceMapper;
import com.Wojtek03.invoice_service.model.Invoice;
import com.Wojtek03.invoice_service.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;

    @BeforeEach
    void setup() {
        invoiceRepository = Mockito.mock(InvoiceRepository.class);
        invoiceMapper = Mockito.mock(InvoiceMapper.class);
        invoiceService = new InvoiceService(invoiceRepository, invoiceMapper);
    }

    @Test
    void processNewOrder_OrderEventNull_ThrowsOrderNotFoundException() {
        assertThrows(OrderNotFoundException.class, () -> {
            invoiceService.processNewOrder(null);
        });
    }

    @Test
    void getInvoiceById_InvoiceExists_ReturnsInvoiceDto() {
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(new InvoiceDto());

        InvoiceDto result = invoiceService.getInvoiceById(invoiceId);

        assertNotNull(result);
        verify(invoiceRepository, times(1)).findById(invoiceId);
    }

    @Test
    void getInvoiceById_InvoiceNotFound_ThrowsInvoiceNotFoundException() {
        Long invoiceId = 1L;

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceService.getInvoiceById(invoiceId);
        });
    }

    @Test
    void getAllInvoices_ReturnsListOfInvoiceDtos() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);

        when(invoiceRepository.findAll()).thenReturn(Collections.singletonList(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(new InvoiceDto());

        List<InvoiceDto> result = invoiceService.getAllInvoices();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository, times(1)).findAll();
    }
}