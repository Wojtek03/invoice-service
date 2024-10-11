package com.Wojtek03.invoice_service.service;

import com.Wojtek03.invoice_service.dto.InvoiceDto;
import com.Wojtek03.invoice_service.dto.OrderEventDto;
import com.Wojtek03.invoice_service.exceptions.InvoiceNotFoundException;
import com.Wojtek03.invoice_service.exceptions.OrderNotFoundException;
import com.Wojtek03.invoice_service.mapper.InvoiceMapper;
import com.Wojtek03.invoice_service.model.Invoice;
import com.Wojtek03.invoice_service.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @KafkaListener(topics = "${order-service.new-orders.topic}", groupId = "invoice-group")
    public void processNewOrder(OrderEventDto orderEvent) {
        log.info("Received OrderEvent: {}", orderEvent);

        if (orderEvent == null || orderEvent.getOrderId() == null) {
            log.error("Received invalid order event: {}", orderEvent);
            throw new OrderNotFoundException(null);
        }

        Long orderId = orderEvent.getOrderId();
        log.info("Processing order with ID: {}", orderId);

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setCreatedAt(LocalDateTime.now());

        log.info("Saving invoice for order: {}", orderId);
        invoiceRepository.save(invoice);

        log.info("Generating invoice file for invoice ID: {}", invoice.getId());
        generateInvoiceFile(invoice);
    }

    public InvoiceDto getInvoiceById(Long id) {
        log.info("Fetching invoice with ID: {}", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Cannot find invoice with that ID"));
        return invoiceMapper.toDto(invoice);
    }

    public List<InvoiceDto> getAllInvoices() {
        log.info("Fetching all invoices");
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    private void generateInvoiceFile(Invoice invoice) {
        String directoryPath = "invoices";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = directoryPath + "/invoice_" + invoice.getId() + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Invoice ID: " + invoice.getId() + "\n");
            writer.write("Order ID: " + invoice.getOrderId() + "\n");
            writer.write("Created At: " + invoice.getCreatedAt() + "\n");
            log.info("Saved invoice file: {}", fileName);
        } catch (IOException e) {
            log.error("Error while generating invoice file", e);
            throw new RuntimeException("Error while generating invoice file", e);
        }
    }
}