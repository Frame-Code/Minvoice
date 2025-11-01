package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.exceptions.FileNotCopyException;
import com.minvoice.demo.application.services.interfaces.IFileService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
@CommonsLog
public class FileServiceImpl implements IFileService {

    @Override
    public void copyFile(File origin, String newPath) {
        try{
            Path source = origin.toPath();
            Path target = Path.of(newPath, origin.getName());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            log.error("Error coping file: " + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new FileNotCopyException();
        }
    }
}
