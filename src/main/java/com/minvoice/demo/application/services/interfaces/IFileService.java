package com.minvoice.demo.application.services.interfaces;

import java.io.File;

public interface IFileService {
    void copyFile(File origin, String newPath);
}
