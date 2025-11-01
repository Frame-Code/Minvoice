package com.minvoice.demo.application.services.dto;

import com.minvoice.demo.domain.model.enums.TypeInvoice;

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
        String fileName,
        String filePath
        ) {
}
