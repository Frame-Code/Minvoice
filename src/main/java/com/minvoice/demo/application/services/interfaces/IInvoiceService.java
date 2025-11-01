package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.application.services.dto.InvoiceDto;
import com.minvoice.demo.application.services.dto.InvoiceTableDto;

import java.util.List;

public interface IInvoiceService {
    void save(InvoiceDto invoice);
    List<InvoiceTableDto> FindAllToTable();
}
