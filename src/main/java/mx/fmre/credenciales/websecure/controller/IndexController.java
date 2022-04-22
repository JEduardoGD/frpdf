package mx.fmre.credenciales.websecure.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import mx.fmre.credenciales.websecure.dto.UploadResult;
import mx.fmre.credenciales.websecure.service.IPdfSecureService;
import mx.fmre.credenciales.websecure.staticv.StaticValuesUtil;

@Controller
@ControllerAdvice
public class IndexController {
    @Autowired
    private IPdfSecureService pdfSecureService;
    
    @Value("${APP_CONTEXT}")
    private String appContext;

    @GetMapping
    public String showUserList(Model model) {
        return StaticValuesUtil.INDEX_REDIRECT;
    }

    @PostMapping(StaticValuesUtil.UPLOAD_FILE_REDIRECT)
    public RedirectView uploadFile(@RequestParam(StaticValuesUtil.FILE_PARAM) MultipartFile file, RedirectAttributes attributes) {
        UploadResult uploadResult = pdfSecureService.uploadFile(file);
        attributes.addFlashAttribute(StaticValuesUtil.MESSAGE_VAR, uploadResult.getMessage());
        attributes.addFlashAttribute(StaticValuesUtil.HAY_ERROR_VAR, uploadResult.isHayError());
        attributes.addFlashAttribute(StaticValuesUtil.FILENAME_VAR, uploadResult.getDownloadFilename());
        return new RedirectView(appContext + StaticValuesUtil.HOME_REDIRECT, true);
    }

    @GetMapping(StaticValuesUtil.DOWNLOAD_FILE_REDIRECT)
    public ResponseEntity<InputStreamResource> downloadFile(@PathParam(StaticValuesUtil.FILENAME_PARAM) String filename)
            throws FileNotFoundException {

        UploadResult uploadResult = pdfSecureService.downloadFile(filename);

        // Download file with InputStreamResource
        FileInputStream fileInputStream = new FileInputStream(uploadResult.getFile());
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + uploadResult.getFile().getName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(uploadResult.getFile().length())
                .body(inputStreamResource);
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(RedirectAttributes attributes) {
        attributes.addFlashAttribute(StaticValuesUtil.MESSAGE_VAR, StaticValuesUtil.MAX_FILESIZE_MESSAGE);
        attributes.addFlashAttribute(StaticValuesUtil.HAY_ERROR_VAR, true);
        return StaticValuesUtil.HOME_REDIRECT;
    }
}
