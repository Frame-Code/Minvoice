package com.minvoice.demo.application.services.dto;

import java.time.LocalDateTime;

public record InvoiceFileDto(
        String noInvoice,
        LocalDateTime issueDate,
        String itemDescription
) {
}
