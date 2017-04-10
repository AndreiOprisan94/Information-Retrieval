package fmi.unibuc.ro.util;

import java.io.File;
import java.io.FileFilter;
import java.util.stream.Stream;

public final class DataDirectoryParser {

    public static File[] getOnlyTextFiles(String dataDirectory){
        FileFilter fileFilter = new TextFileFilter();
        File dataDirFiles = new File(dataDirectory);

        return Stream.of(dataDirFiles.listFiles())
                .filter(fileFilter::accept)
                .toArray(File[] :: new);

    }
}
