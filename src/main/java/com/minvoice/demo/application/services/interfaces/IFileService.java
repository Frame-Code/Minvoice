package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.application.services.dto.ResponseDto;

import java.io.File;

public interface IFileService {
    void copyFile(File origin, String newPath);
    ResponseDto openInFileExplorer(File file);
}
