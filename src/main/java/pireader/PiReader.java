package pireader;

import java.io.IOException;

public interface PiReader {
    int[] getData(int offset, int len) throws IOException;
}
