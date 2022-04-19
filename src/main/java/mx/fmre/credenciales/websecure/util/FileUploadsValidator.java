package mx.fmre.credenciales.websecure.util;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public abstract class FileUploadsValidator {
    protected boolean isEmptyFile(MultipartFile file) {
        return file.isEmpty();
    }

    protected boolean isInvalidFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || "".equals(originalFilename)) {
            return true;
        }
        return !originalFilename.toLowerCase().endsWith("pdf");
    }

    protected boolean canReadFile(File file) {
        return file.canRead();
    }

    protected boolean fileExists(File file) {
        return file.exists();
    }
}
