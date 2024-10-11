package com.Wojtek03.invoice_service.mapper;

import com.Wojtek03.invoice_service.dto.InvoiceDto;
import com.Wojtek03.invoice_service.model.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDto toDto(Invoice invoice);

    Invoice toEntity(InvoiceDto invoiceDto);
}

