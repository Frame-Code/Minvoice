package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.application.services.dto.InvoiceFileDto;

public interface IXmlInvoiceReader {
    InvoiceFileDto read(String filePath);
}
