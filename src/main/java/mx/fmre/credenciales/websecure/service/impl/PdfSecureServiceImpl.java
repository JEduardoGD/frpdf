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

import lombok.extern.slf4j.Slf4j;
import mx.fmre.credenciales.websecure.dto.UploadResult;
import mx.fmre.credenciales.websecure.exception.WebsecureException;
import mx.fmre.credenciales.websecure.service.IPdfSecureService;
import mx.fmre.credenciales.websecure.staticv.StaticValuesUtil;
import mx.fmre.credenciales.websecure.util.FileUploadsValidator;
import mx.fmre.credenciales.websecure.util.PdfEncryptUtil;

@Service
@Slf4j
public class PdfSecureServiceImpl extends FileUploadsValidator implements IPdfSecureService {
    @Value("${UPLOAD_DIR}")
    private String uploadDir;

    @Value("${UPLOAD_MODIFIED_DIR}")
    private String uploadModifiedDir;

    private final DateFormat DATE_FORMAT = new SimpleDateFormat(StaticValuesUtil.UPLOADEDFILE_DATE_FORMAT);

    @Override
    public UploadResult uploadFile(MultipartFile file) {
        if (isEmptyFile(file)) {
            return new UploadResult(true, StaticValuesUtil.MUST_SELECT_FILE_MESSAGE);
        }

        if (isInvalidFileExtension(file)) {
            return new UploadResult(true, StaticValuesUtil.INVALID_FILENAME_MESSAGE);
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        Path path = Paths.get(
                uploadDir + File.separator + DATE_FORMAT.format(Calendar.getInstance().getTime()) + "_" + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return new UploadResult(true, String.format(StaticValuesUtil.ERROR_UPLOADING_MESSAGE, e.getLocalizedMessage()));
        }

        try {
            PdfEncryptUtil.removeWrights(path.toFile(), uploadModifiedDir + File.separator + fileName);
        } catch (WebsecureException e) {
            log.error(e.getLocalizedMessage());
            return new UploadResult(true, String.format(StaticValuesUtil.ERROR_UPLOADING_MESSAGE, e.getLocalizedMessage()));
        }

        return new UploadResult(false, StaticValuesUtil.UPLOAD_SUCCESSFULLY_MESSAGE, fileName);
    }

    @Override
    public UploadResult downloadFile(String filename) {
        String path = uploadModifiedDir + File.separator + filename;
        File file = Paths.get(path).toFile();
        if(!fileExists(file)) {
            return new UploadResult(true, StaticValuesUtil.FILE_DOES_NOT_EXISTS_MESSAGE);
        }
        
        if(!canReadFile(file)) {
            return new UploadResult(true, StaticValuesUtil.CANT_READ_FILE_MESSAGE);
        }

        return new UploadResult(false, file);
        
    }
}
