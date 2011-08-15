package tools;

import java.io.File;

public class Tools {
	
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
            // bug correction
            ext.replaceAll("\\s", "");
        }
        return ext;
    }

}
