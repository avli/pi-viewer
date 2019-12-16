import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PiMemoryReader implements PiReadStrategy {

    private final String myFilePath;
    private final List<Integer> myRgbBuffer = new ArrayList<>();

    public PiMemoryReader(URL fileURI) {
        this(fileURI.getPath());
    }

    public PiMemoryReader(String filePath) {
        Objects.requireNonNull(filePath);
        myFilePath = filePath;
    }

    public void loadPi() {
        try (final BufferedReader br = Files.newBufferedReader(Paths.get(myFilePath))) {
            br.skip(2); // Skip "3."
            char[] cbuf = new char[8];
            int charsRead;
            while ((charsRead = br.read(cbuf)) != -1) {
                if (charsRead < 8) {
                    return;
                }
                myRgbBuffer.add(Integer.parseInt(String.valueOf(cbuf)));
            }
        }
        catch (IOException e) {
            System.err.println("Error while loading PI file " + myFilePath);
            e.printStackTrace();
        }
    }

    public int[] getData(int offset, int len) throws IndexOutOfBoundsException {
        loadPi();
        if (myRgbBuffer.size() - offset < len) {
            throw new IndexOutOfBoundsException("Not enough data: pi values buffer contains " + myRgbBuffer.size() +
                    " points, " + len + " requested with offset " + offset + ".");
        }
        int[] data = new int[len];
        for (int i = 0; i < data.length; offset++, i++) {
            data[i] = myRgbBuffer.get(offset);
        }
        return data;
    }

    public void saveToFile(String filePath) {
        try (FileWriter fw = new FileWriter(filePath)) {
            for (Integer i : myRgbBuffer) {
                fw.write(Integer.valueOf(i >> 16 & 0xff).toString());
                fw.write(", ");
                fw.write(Integer.valueOf(i >> 8 & 0xff).toString());
                fw.write(", ");
                fw.write(Integer.valueOf(i & 0xff).toString());
                fw.write("\n");
            }
        }
        catch (IOException e) {
            System.err.println("Can't open file for writing: " + filePath);
            e.printStackTrace();
        }
    }
}
