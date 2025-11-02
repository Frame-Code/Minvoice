package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.dto.PaymentDto;
import com.minvoice.demo.application.services.exceptions.InvoiceNotFoundException;
import com.minvoice.demo.application.services.exceptions.StatusNotFoundException;
import com.minvoice.demo.application.services.interfaces.IPaymentService;
import com.minvoice.demo.domain.model.GeneralStatus;
import com.minvoice.demo.domain.model.Invoice;
import com.minvoice.demo.domain.model.PaymentDate;
import com.minvoice.demo.domain.repository.IGeneralStatusRepository;
import com.minvoice.demo.domain.repository.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@CommonsLog
public class PaymentServiceImpl implements IPaymentService {
    private final IInvoiceRepository invoiceRepository;
    private final IGeneralStatusRepository statusRepository;

    @Override
    public void addPayment(PaymentDto paymentDto) {
        Optional<Invoice> invoice = invoiceRepository.findById(paymentDto.idInvoice());
        if(invoice.isEmpty()) {
            log.error("Error: invoice with id " + paymentDto.idInvoice() + "was not found");
            throw new InvoiceNotFoundException();
        }
        Optional<GeneralStatus> status = statusRepository.FindByCode(paymentDto.statusCode());
        if(status.isEmpty()) {
            log.error("Error: status with code " + paymentDto.statusCode() + "was not found");
            throw new StatusNotFoundException();
        }

        var payment = PaymentDate.builder()
                .amount(paymentDto.amount())
                .type(status.get())
                .observation(paymentDto.observation())
                .date(paymentDto.paymentDate())
                .invoice(invoice.get())
                .build();
        invoice.get().addPayment(payment);
        invoiceRepository.save(invoice.get());

    }
}
