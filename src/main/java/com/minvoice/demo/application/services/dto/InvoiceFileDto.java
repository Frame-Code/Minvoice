package com.minvoice.demo.application.services.dto;

import java.time.LocalDateTime;
import java.util.List;

public record InvoiceFileDto(
        String noInvoice,
        LocalDateTime issueDate,
        List<String> itemDescriptions
) {
}
