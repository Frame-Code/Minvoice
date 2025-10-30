package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.exceptions.FileNotReadException;
import com.minvoice.demo.application.services.interfaces.IPdfReaderService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;

@Service
@CommonsLog
public class PdfReaderServiceImpl implements IPdfReaderService {

    @Override
    public String readText(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception e) {
            log.error("Error reading file: " + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new FileNotReadException();
        }
    }
}
