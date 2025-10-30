package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.exceptions.FileNotReadException;
import com.minvoice.demo.application.services.interfaces.IXmlReader;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;

@Service
@CommonsLog
public class XmlReaderImpl implements IXmlReader {

    @Override
    public Document read(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            log.error("Error reading file: " + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new FileNotReadException();
        }

    }
}
