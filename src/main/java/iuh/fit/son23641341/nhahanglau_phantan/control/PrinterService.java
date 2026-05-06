package iuh.fit.son23641341.nhahanglau_phantan.control;

import java.io.File;

// NOTE: PDF printing removed; mock implementation only validates file existence.
public class PrinterService {

    public static boolean printPDF(String filePath, String printerName) {
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }
}
