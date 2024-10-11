//package com.Wojtek03.invoice_service.controller;
//
//import com.Wojtek03.invoice_service.dto.InvoiceDto;
//import com.Wojtek03.invoice_service.service.InvoiceService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class InvoiceControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private InvoiceService invoiceService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void getInvoiceById_ReturnsInvoice() throws Exception {
//        InvoiceDto invoiceDto = new InvoiceDto();
//        invoiceDto.setId(1L);
//
//        Mockito.when(invoiceService.getInvoiceById(anyLong())).thenReturn(invoiceDto);
//
//        mockMvc.perform(get("/invoices/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(invoiceDto.getId()));
//    }
//
//    @Test
//    void getAllInvoices_ReturnsListOfInvoices() throws Exception {
//        InvoiceDto invoiceDto = new InvoiceDto();
//        invoiceDto.setId(1L);
//
//        Mockito.when(invoiceService.getAllInvoices()).thenReturn(List.of(invoiceDto));
//
//        mockMvc.perform(get("/invoices")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(invoiceDto.getId())); // Ścieżka do pierwszego elementu
//    }
//}