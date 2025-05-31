package sendfile;

public enum FileSize {
    SIZE_1MB(1024 * 1024),
    SIZE_10MB(10 * 1024 * 1024),
    SIZE_50MB(50 * 1024 * 1024),
    SIZE_100MB(100 * 1024 * 1024),
    SIZE_250MB(250 * 1024 * 1024),
    SIZE_500MB(500 * 1024 * 1024),
    SIZE_1GB(1024 * 1024 * 1024);

    private final int bytes;

    FileSize(int bytes) {
        this.bytes = bytes;
    }

    public int getBytes() {
        return bytes;
    }
}
