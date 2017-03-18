package fmi.unibuc.ro.indexer;

import java.io.File;
import java.io.FileFilter;

public class TextFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return isTxt(pathname) && isNotDirectory(pathname) && isNotHidden(pathname) && existsAndCanRead(pathname);
    }

    private boolean isTxt(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }

    private boolean isNotDirectory(File pathname) {
        return !pathname.isDirectory();
    }

    private boolean isNotHidden(File pathname) {
        return !pathname.isHidden();
    }

    private boolean existsAndCanRead(File pathname) {
        return pathname.exists() && pathname.canRead();
    }
}
