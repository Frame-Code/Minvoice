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
        var invoices = repository.findAll()
                .stream().toList();
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTotal();
        }
        return total;

    }

    @Override
    public double getTotalPaid() {
        var invoices = repository.findAll()
                .stream()
                .filter(invoice -> invoice.getStatus().getCode().trim().equalsIgnoreCase("cnd") ||
                        invoice.getStatus().getCode().trim().equalsIgnoreCase("pp"))
                .toList();
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTotalPayments();
        }
        return total;
    }

    @Override
    public double getPaymentDue() {
        return getTotalBilled() - getTotalPaid();
    }

    @Override
    public int getInvoiceCount() {
        return repository.findAll().size();
    }
}
