package sendfile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransferToServer {

    ServerSocketChannel listener = null;

    protected void mySetup() {
        InetSocketAddress listenAddr = new InetSocketAddress(2001);

        try {
            listener = ServerSocketChannel.open();
            ServerSocket ss = listener.socket();
            ss.setReuseAddress(true);
            ss.bind(listenAddr);
            System.out.println("Server waiting for client on port " + listenAddr.getPort());
        } catch (IOException e) {
            System.out.println("Failed to bind, is port : " + listenAddr + " already in use? Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TransferToServer server = new TransferToServer();
        server.mySetup();
        server.readData();
    }

    private void readData() {
        int fileSize = (int) (FileSize.SIZE_10MB.getBytes() * 1.1);
        ByteBuffer dst = ByteBuffer.allocate(fileSize);

        try {
            while (true) {
                SocketChannel conn = listener.accept();
                System.out.println("Accepted: " + conn.getRemoteAddress());
                conn.configureBlocking(true);

                long totalBytesRead = 0;
                int nread;
                long startTime = System.currentTimeMillis();

                while ((nread = conn.read(dst)) != -1) {
                    totalBytesRead += nread;
                    dst.clear(); // reset position & limit
                }

                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("전송 완료: 총 바이트 = " + totalBytesRead + ", 소요 시간(ms) = " + elapsed);

                conn.close(); // 연결 종료
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
