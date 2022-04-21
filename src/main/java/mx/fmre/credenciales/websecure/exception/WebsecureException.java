package mx.fmre.credenciales.websecure.exception;

public class WebsecureException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4548232987033491234L;

    public WebsecureException(String message) {
        super(message);
    }

    public WebsecureException(Exception e) {
        super(e);
    }

}
