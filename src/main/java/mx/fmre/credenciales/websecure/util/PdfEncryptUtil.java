package mx.fmre.credenciales.websecure.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import mx.fmre.credenciales.websecure.exception.WebsecureException;

public final class PdfEncryptUtil {
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
            // StandardProtectionPolicy spp = new StandardProtectionPolicy("1234",
            // "fmre2022", ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);
            document.protect(spp);
            System.out.println("PDF Document encrypted Successfully.");
            System.out.println("CHECK " + file.getPath());

            File f = Paths.get(newFilepath).toFile();
            if (f.exists() && f.canWrite()) {
                f.delete();
            }
            if (f.exists() && !f.canWrite()) {
                throw new WebsecureException("No es posible borrar el archivo");
            }

            document.save(newFilepath);
        } catch (IOException e) {
            throw new WebsecureException(e);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    throw new WebsecureException(e);
                }
            }
        }
    }

}
