package mx.fmre.credenciales.websecure.dto;

import java.io.File;

public class UploadResult {
    private boolean hayError;
    private String message;
    private String downloadFilename;
    private File file;

    public UploadResult(boolean hayError, String message) {
        super();
        this.hayError = hayError;
        this.message = message;
    }

    public UploadResult(boolean hayError, String message, String downloadFilename) {
        super();
        this.hayError = hayError;
        this.message = message;
        this.downloadFilename = downloadFilename;
    }

    public UploadResult(boolean hayError, File file) {
        super();
        this.hayError = hayError;
        this.file = file;
    }

    public boolean isHayError() {
        return hayError;
    }

    public void setHayError(boolean hayError) {
        this.hayError = hayError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDownloadFilename() {
        return downloadFilename;
    }

    public void setDownloadFilename(String downloadFilename) {
        this.downloadFilename = downloadFilename;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}