package mx.fmre.credenciales.websecure.service;

import org.springframework.web.multipart.MultipartFile;

import mx.fmre.credenciales.websecure.dto.UploadResult;

public interface IPdfSecureService {

    UploadResult uploadFile(MultipartFile file);

    UploadResult downloadFile(String filename);

}
