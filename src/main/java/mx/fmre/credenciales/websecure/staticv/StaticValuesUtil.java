package mx.fmre.credenciales.websecure.staticv;

public abstract class StaticValuesUtil {
    private StaticValuesUtil() {
        // no call
    }

    //redirect variabl names
    public static final String MESSAGE_VAR = "message";
    public static final String HAY_ERROR_VAR = "hayError";
    public static final String FILENAME_VAR = "filename";
    
    //redirect values
    public static final String INDEX_REDIRECT = "index";
    public static final String HOME_REDIRECT = "redirect:/";
    public static final String HOMES_REDIRECT = "/";
    public static final String DOWNLOAD_FILE_REDIRECT = "/downloadfile";
    public static final String UPLOAD_FILE_REDIRECT = "/upload";
    
    //param names
    public static final String FILE_PARAM = "file";
    public static final String FILENAME_PARAM = "filename";
    
    //messages
    public static final String MAX_FILESIZE_MESSAGE = "El archivo excede el tamaño máximo permitido";
    public static final String MUST_SELECT_FILE_MESSAGE = "Debe seleccionar un archivo para cargar";
    public static final String INVALID_FILENAME_MESSAGE = "El nombre o la extensión del archivo no es valido";
    public static final String ERROR_UPLOADING_MESSAGE = "Error al cargar el archivo: %s";
    public static final String UPLOAD_SUCCESSFULLY_MESSAGE = "Archivo cargado correctamente";
    public static final String FILE_DOES_NOT_EXISTS_MESSAGE = "El archivo no se puede leer";
    public static final String CANT_READ_FILE_MESSAGE = "El archivo no se puede leer";
    public static final String CANT_DELETE_FILE_MESSAGE = "El archivo no se puede borrar";
    
    public static final String UPLOADEDFILE_DATE_FORMAT = "yyyyMMddHHmmss";
}
