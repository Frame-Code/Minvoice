package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.application.services.dto.InvoiceDto;

public interface IInvoiceService {
    void save(InvoiceDto invoice);
}
