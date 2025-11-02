package com.minvoice.demo.application.services.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddPaymentDto(
        BigDecimal mount,
        LocalDateTime date
) {
}
