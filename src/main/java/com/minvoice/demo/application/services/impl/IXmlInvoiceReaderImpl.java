package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.dto.InvoiceFileDto;
import com.minvoice.demo.application.services.exceptions.FileNotReadException;
import com.minvoice.demo.application.services.interfaces.IXmlInvoiceReader;
import com.minvoice.demo.application.services.interfaces.IXmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@CommonsLog
@RequiredArgsConstructor
public class IXmlInvoiceReaderImpl implements IXmlInvoiceReader {
    private final IXmlReader reader;

    @Override
    public InvoiceFileDto read(String filePath) {
        try {
            Document doc = reader.read(filePath);
            XPath xpath = XPathFactory.newInstance().newXPath();

            String noInvoice =
                    doc.getElementsByTagName("estab").item(0).getTextContent() + "-" +
                            doc.getElementsByTagName("ptoEmi").item(0).getTextContent() + "-" +
                            doc.getElementsByTagName("secuencial").item(0).getTextContent();

            NodeList nodes = (NodeList) xpath.evaluate("//detalle/codigoPrincipal", doc, XPathConstants.NODESET);
            List<String> descriptions = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                descriptions.add(nodes.item(i).getTextContent());
            }

            String expression = "//*[local-name()='SigningTime']";
            Node node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
            String value = node.getTextContent();
            // Convertir a LocalDateTime
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            LocalDateTime issueDate = offsetDateTime.toLocalDateTime();

            return new InvoiceFileDto(noInvoice, issueDate, descriptions);
        } catch (Exception e) {
            log.error("Error reading file: " + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new FileNotReadException();
        }
    }
}
