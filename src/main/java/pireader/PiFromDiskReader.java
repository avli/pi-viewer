package pireader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * For huge files. Reads holds only current bytes in memory.
 */
public class PiFromDiskReader extends PiFileReader {

    public PiFromDiskReader(String filePath) {
        super(filePath);
    }

    public PiFromDiskReader(URL fileURI) {
        super(fileURI);
    }

    @Override
    public int[] getData(int offset, int len) throws IOException {
        BufferedReader br = Files.newBufferedReader(Paths.get(myFilePath));
        skipToOffset(br, offset);
        char[] cbuf = new char[8];
        int charsRead;
        int[] data = new int[len];
        for(int i = 0; i < len; i++) {
            if ((charsRead = br.read(cbuf)) != -1) {
                if (charsRead < 8)
                    throw new IndexOutOfBoundsException("Not enough data to read " + len + " points from file "
                            + myFilePath + " with offset " + offset + ".");
                data[i] = Integer.parseInt(String.valueOf(cbuf));
            }
        }
        return data;
    }

    private void skipToOffset(BufferedReader br, int offset) throws IllegalArgumentException, IOException {
        br.mark(0);
        br.reset();
        br.skip(2); // Skip "3."
        long bytesToSkip = 8L * offset;
        long actuallySkipped = br.skip(bytesToSkip);
        if (actuallySkipped < bytesToSkip)
            throw new IllegalArgumentException("Not enough data: file contains " + actuallySkipped +
                    " points, requested offset is " + offset + ".");
    }
}
