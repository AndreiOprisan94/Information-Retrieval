package fmi.unibuc.ro.util;

import java.io.File;
import java.io.FileFilter;

class RegularFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return isReg(pathname) && isNotHidden(pathname) && existsAndCanRead(pathname);
    }

    private boolean isReg(File pathname) {
        return pathname.isFile();
    }

    private boolean isNotHidden(File pathname) {
        return !pathname.isHidden();
    }

    private boolean existsAndCanRead(File pathname) {
        return pathname.exists() && pathname.canRead();
    }
}
