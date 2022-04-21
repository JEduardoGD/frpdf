package mx.fmre.credenciales.websecure.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import lombok.extern.slf4j.Slf4j;
import mx.fmre.credenciales.websecure.exception.WebsecureException;
import mx.fmre.credenciales.websecure.staticv.StaticValuesUtil;

@Slf4j
public final class PdfEncryptUtil {
    private PdfEncryptUtil() {
        // no call
    }

    public static void removeWrights(File file, String newFilepath) throws WebsecureException {
        PDDocument document = null;
        try {
            document = PDDocument.load(file);

            AccessPermission ap = new AccessPermission();
            ap.setCanExtractContent(false);
            ap.setCanModify(false);
            ap.setCanExtractForAccessibility(false);
            ap.canExtractContent();
            ap.canModify();
            ap.canExtractForAccessibility();

            StandardProtectionPolicy spp = new StandardProtectionPolicy("fmre2022", null, ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);
            document.protect(spp);
            log.info("PDF Document encrypted Successfully.");
            log.info("CHECK {}", file.getPath());

            File f = Paths.get(newFilepath).toFile();
            if (f.exists() && f.canWrite()) {
                if (!f.delete()) {
                    log.error(StaticValuesUtil.CANT_DELETE_FILE_MESSAGE);
                }
            }
            if (f.exists() && !f.canWrite()) {
                throw new WebsecureException(StaticValuesUtil.CANT_DELETE_FILE_MESSAGE);
            }

            document.save(newFilepath);
        } catch (IOException e) {
            throw new WebsecureException(e);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage());
                }
            }
        }
    }

}
