package mx.fmre.credenciales.websecure.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import mx.fmre.credenciales.websecure.dto.UploadResult;
import mx.fmre.credenciales.websecure.service.IPdfSecureService;

@Controller
@ControllerAdvice
public class IndexController {
    @Autowired
    private IPdfSecureService pdfSecureService;

    @GetMapping
    public String showUserList(Model model) {
        // model.addAttribute("users", Arrays.asList(user));
        return "index";
    }

    @PostMapping("/upload")
    public RedirectView uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        UploadResult uploadResult = pdfSecureService.uploadFile(file);
        attributes.addFlashAttribute("message", uploadResult.getMessage());
        attributes.addFlashAttribute("hayError", uploadResult.isHayError());
        attributes.addFlashAttribute("filename", uploadResult.getDownloadFilename());
        return new RedirectView("/", true);
    }

    @RequestMapping(value = "/downloadfile", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadFile(@PathParam("filename") String filename)
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
        attributes.addFlashAttribute("message", "Max filesize");
        attributes.addFlashAttribute("hayError", true);
        return "redirect:/";
    }
}
