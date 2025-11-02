package com.minvoice.demo.application.services.dto;

import java.time.LocalDate;

public record PaymentDto(
        double amount,
        String observation,
        String statusCode,
        LocalDate paymentDate,
        int idInvoice
) {
}
