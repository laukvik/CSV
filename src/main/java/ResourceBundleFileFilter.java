import org.laukvik.csv.io.AbstractResourceBundle;

import java.io.File;
import java.io.FileFilter;

/**
 *
 *
 */
public class ResourceBundleFileFilter implements FileFilter{

    private String basename;

    /**
     *
     * @param basename
     */
    public ResourceBundleFileFilter(final String basename) {
        this.basename = basename;
    }

    @Override
    public boolean accept(final File file) {
        if (file == null || file.isDirectory()) {
            return false;
        }
        return AbstractResourceBundle.getLocale(file.getName(), basename) != null;
    }
}
