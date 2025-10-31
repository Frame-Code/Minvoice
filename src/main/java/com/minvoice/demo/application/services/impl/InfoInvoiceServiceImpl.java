package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.interfaces.IInfoInvoiceService;
import com.minvoice.demo.domain.model.Invoice;
import com.minvoice.demo.domain.repository.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CommonsLog
public class InfoInvoiceServiceImpl implements IInfoInvoiceService {

    private final IInvoiceRepository repository;

    @Override
    public double getTotalBilled() {
        return repository.findAll()
                .stream()
                .mapToDouble(Invoice::getTotal)
                .sum();

    }

    @Override
    public double getTotalPaid() {
        return repository.findAll()
                .stream()
                .filter(invoice -> invoice.getStatus().getCode().trim().equalsIgnoreCase("cnld") ||
                        invoice.getStatus().getCode().trim().equalsIgnoreCase("pp"))
                .mapToDouble(Invoice::getTotal)
                .sum();
    }

    @Override
    public double getPaymentDue() {
        List<Invoice> invoices = repository.findAll();
        double amountPending = invoices.stream()
                .filter(invoice -> invoice.getStatus().getCode().trim().equalsIgnoreCase("pnd"))
                .mapToDouble(Invoice::getTotal)
                .sum();
        double amountPartiallyPaid = invoices.stream()
                .filter(invoice -> invoice.getStatus().getCode().trim().equalsIgnoreCase("pp"))
                .mapToDouble(Invoice::getTotalPayments)
                .sum();
        double amountPartiallyPaidTotal = invoices.stream()
                .filter(invoice -> invoice.getStatus().getCode().trim().equalsIgnoreCase("pp"))
                .mapToDouble(Invoice::getTotal)
                .sum();

        return amountPending + (amountPartiallyPaidTotal - amountPartiallyPaid);
    }

    @Override
    public int getInvoiceCount() {
        return repository.findAll().size();
    }
}
