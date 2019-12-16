import java.net.URL;
import java.util.Objects;

public class PiReaderContext {

    private PiReadStrategy myReadStrategy;
    private final String myFilePath;

    public static class Builder {

        private String myFilePath;
        private PiReadStrategy myReadStrategy;

        public Builder setFilePath(String filePath) {
            myFilePath = filePath;
            return this;
        }

        public Builder setFilePath(URL fileURI) {
            return setFilePath(fileURI.getPath());
        }

        public Builder setReadStrategy(PiReadStrategy readStrategy) {
            myReadStrategy = readStrategy;
            return this;
        }

        public PiReaderContext build() {
            if (myFilePath == null)
                throw new IllegalArgumentException(
                        "File path must be set through `setFilePath()` builder method.");
            if (myReadStrategy == null) throw new IllegalArgumentException(
                    "Read strategy must be set through `setReadStrategy()` builder method.");
            return new PiReaderContext(myFilePath, myReadStrategy);
        }
    }

    private PiReaderContext(String filePath, PiReadStrategy readStrategy) {
        myFilePath = filePath;
        myReadStrategy = readStrategy;
    }

    public int[] getData(int offset, int width, int height) {
        return myReadStrategy.getData(offset, width);
    }
}
