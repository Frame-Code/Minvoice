package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.dto.ResponseDto;
import com.minvoice.demo.application.services.exceptions.FileNotCopyException;
import com.minvoice.demo.application.services.interfaces.IFileService;
import javafx.scene.control.Alert;
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

    @Override
    public ResponseDto openInFileExplorer(File file) {
        if (file == null || !file.exists()) {
            return new ResponseDto(
                    "El archivo no existe o la ruta no es válida:\n" + (file != null ? file.getAbsolutePath() : ""),
                    false);
        }

        try {
            File directory = file.isDirectory() ? file : file.getParentFile();
            if (directory == null) {
                return new ResponseDto("Error, el directorio no existe", false);
            }

            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("explorer.exe", "/select,", file.getAbsolutePath()).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", directory.getAbsolutePath()).start();
            } else {
                new ProcessBuilder("xdg-open", directory.getAbsolutePath()).start();
            }
            return new ResponseDto(null, true);

        } catch (Exception e) {
            log.error("Error opening file explorer: " + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            return new ResponseDto("Error interno, revisar logs", false);
        }
    }
}
