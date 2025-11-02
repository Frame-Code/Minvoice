package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.application.services.dto.PaymentDto;

import java.time.LocalDate;

public interface IPaymentService {
    void addPayment(PaymentDto paymentDto);
}
