package com.minvoice.demo.application.services.dto;

import com.minvoice.demo.domain.enums.TypeInvoice;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceDto(
        String noInvoice,
        TypeInvoice typeInvoice,
        String statusCode,
        String description,
        String observation,
        LocalDateTime issueDate,
        List<String> itemsCode,
        File pdfFile,
        File xmlFile
        ) {
}
