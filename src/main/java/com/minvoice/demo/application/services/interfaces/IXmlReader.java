package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.application.services.dto.InvoiceFileDto;
import org.w3c.dom.Document;

public interface IXmlReader {
    Document read(String filePath);
}
