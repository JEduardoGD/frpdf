package mx.fmre.credenciales.websecure.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import mx.fmre.credenciales.websecure.dto.UploadResult;
import mx.fmre.credenciales.websecure.exception.WebsecureException;
import mx.fmre.credenciales.websecure.service.IPdfSecureService;
import mx.fmre.credenciales.websecure.util.FileUploadsValidator;
import mx.fmre.credenciales.websecure.util.PdfEncryptUtil;

@Service
public class PdfSecureServiceImpl extends FileUploadsValidator implements IPdfSecureService {
    @Value("${UPLOAD_DIR}")
    private String UPLOAD_DIR;

    @Value("${UPLOAD_MODIFIED_DIR}")
    private String UPLOAD_MODIFIED_DIR;

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public UploadResult uploadFile(MultipartFile file) {
        if (isEmptyFile(file)) {
            return new UploadResult(true, "Seleccione un archivo a cargar");
        }

        if (isInvalidFileExtension(file)) {
            return new UploadResult(true, "El nombre o la extensión del archivo no es valido");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        Path path = Paths.get(
                UPLOAD_DIR + File.separator + DATE_FORMAT.format(Calendar.getInstance().getTime()) + "_" + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return new UploadResult(true, String.format("Error al cargar el archivo: %s}", e.getLocalizedMessage()));
        }

        try {
            PdfEncryptUtil.removeWrights(path.toFile(), UPLOAD_MODIFIED_DIR + File.separator + fileName);
        } catch (WebsecureException e) {
            e.printStackTrace();
            return new UploadResult(true, String.format("Error al cargar el archivo: %s}", e.getLocalizedMessage()));
        }

        return new UploadResult(false, "Archivo cargado correctamente", fileName);
    }

    @Override
    public UploadResult downloadFile(String filename) {
        String path = UPLOAD_MODIFIED_DIR + File.separator + filename;
        File file = Paths.get(path).toFile();
        if(!fileExists(file)) {
            return new UploadResult(true, "El archivo no existe");
        }
        
        if(!canReadFile(file)) {
            return new UploadResult(true, "El archivo no se puede leer");
        }

        return new UploadResult(false, file);
        
    }
}
