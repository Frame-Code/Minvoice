package com.minvoice.demo.application.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceTableDto {
    private int idInvoice;
    private String status;
    private String description;
    private double mount;
    private double porPagar;
    private LocalDateTime date;
    private String fileName;
    private String filePath;
}
