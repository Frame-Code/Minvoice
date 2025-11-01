package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.dto.InvoiceDto;
import com.minvoice.demo.application.services.exceptions.ItemNotFoundException;
import com.minvoice.demo.application.services.exceptions.StatusNotFoundException;
import com.minvoice.demo.application.services.interfaces.IFileService;
import com.minvoice.demo.application.services.interfaces.IInvoiceService;
import com.minvoice.demo.domain.model.*;
import com.minvoice.demo.domain.repository.IGeneralStatusRepository;
import com.minvoice.demo.domain.repository.IInvoiceFileRepository;
import com.minvoice.demo.domain.repository.IItemRepository;
import com.minvoice.demo.application.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {
    private final IInvoiceFileRepository invoiceFileRepository;
    private final IGeneralStatusRepository statusRepository;
    private final IItemRepository itemRepository;
    private final IFileService fileService;
    private final AppProperties properties;

    @Override
    public void save(InvoiceDto invoice) {
        fileService.copyFile(invoice.pdfFile(), properties.getInvoiceFileNewPath() + invoice.pdfFile().getName());
        fileService.copyFile(invoice.xmlFile(), properties.getInvoiceFileNewPath() + invoice.xmlFile().getName());

        //Status
        Optional<GeneralStatus> status = statusRepository.FindByCode(invoice.statusCode());
        if(status.isEmpty()) {
            log.error("Error: status code " + invoice.statusCode() + "was not found");
            throw new StatusNotFoundException();
        }

        //Item
        List<Item> items = itemRepository.findAll();
        List<Item> invoiceItems = new ArrayList<>();
        invoice.itemsCode().forEach(itemCode -> {
            var itemFound = items.stream()
                    .filter(item -> item.getCode().equals(itemCode))
                    .findFirst();
            if(itemFound.isEmpty()) {
                log.error("Error: item with code " + itemCode + "was not found");
                throw new ItemNotFoundException();
            }
            invoiceItems.add(itemFound.get());
        });
        double totalItemPrice = invoiceItems
                .stream()
                .mapToDouble(Item::getPrice)
                .sum();


        //Detail invoice
        List<DetailInvoice> details = new ArrayList<>();
        invoiceItems.forEach(item -> {
            details.add(
                    DetailInvoice.builder()
                    .item(item)
                    .build());
        });

        //Invoice
        Invoice inv = Invoice.builder()
                .typeInvoice(invoice.typeInvoice())
                .noInvoice(invoice.noInvoice())
                .status(status.get())
                .description(invoice.description())
                .observation(invoice.observation())
                .issueDate(invoice.issueDate())
                .total(totalItemPrice)
                .detailInvoices(details)
                .build();

        //Invoice file
        InvoiceFile file = InvoiceFile.builder()
                .fileName(invoice.pdfFile().getName())
                .filePath(properties.getInvoiceFileNewPath() + invoice.pdfFile().getName())
                .invoice(inv)
                .build();

        invoiceFileRepository.save(file);
    }
}
