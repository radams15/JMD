package uk.co.therhys.MD;

import cz.adamh.utils.NativeUtils;

import java.io.IOException;

public class Converter {
    static{
        //System.load("/mnt/share/scripts/java/JMD/src/main/native/markdown.so");

        try {
            String os = System.getProperty("os.name");
            
            System.out.println("OS: " + os);

            if(os.equals("windows")){
                NativeUtils.loadLibraryFromJar("/markdown.dll");
            } else if(os.equals("Mac OS X")){
                NativeUtils.loadLibraryFromJar("/libmarkdown.dylib");
            } else{
                NativeUtils.loadLibraryFromJar("/libmarkdown.so");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public native String md2html(String markdown);
}
