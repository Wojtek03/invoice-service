package com.Wojtek03.invoice_service.repository;

import com.Wojtek03.invoice_service.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
