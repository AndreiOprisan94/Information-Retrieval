package fmi.unibuc.ro.util;

import java.io.File;
import java.io.FileFilter;
import java.util.stream.Stream;

public final class DataDirectoryParser {

    public static File[] retrieveRegularFiles(String dataDirectory){
        FileFilter fileFilter = new RegularFileFilter();
        File[] dataDirFiles = new File(dataDirectory).listFiles();

        return Stream.of(dataDirFiles)
                .filter(fileFilter::accept)
                .toArray(File[] :: new);

    }
}
