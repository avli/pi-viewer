package pireader;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public abstract class PiFileReader implements PiReader {

    protected String myFilePath;

    public PiFileReader(String filePath) {
        Objects.requireNonNull(filePath);
        myFilePath = filePath;
    }

    public PiFileReader(URL fileURI) {
        this(fileURI.getPath());
    }

    @Override
    public abstract int[] getData(int offset, int len) throws IOException;
}
