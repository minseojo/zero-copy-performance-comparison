package sendfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class TransferToClient {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 2001;
		String filePath = "sendfile/10mb.bin";

		TransferToClient client = new TransferToClient();
		try {
			client.sendFile(host, port, filePath);
		} catch (IOException e) {
			System.err.println("파일 전송 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void sendFile(String host, int port, String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("파일이 존재하지 않습니다: " + file.getAbsolutePath());
		}

		try (
				SocketChannel socketChannel = SocketChannel.open();
				FileChannel fileChannel = new FileInputStream(file).getChannel()
		) {
			socketChannel.connect(new InetSocketAddress(host, port));
			socketChannel.configureBlocking(true);
			System.out.println("서버 연결 완료: " + socketChannel.getRemoteAddress());

			long position = 0;
			long fileSize = file.length();
			long totalSent = 0;

			long startTime = System.currentTimeMillis();

			while (position < fileSize) {
				long sent = fileChannel.transferTo(position, fileSize - position, socketChannel);
				if (sent <= 0) break; // 전송 실패
				position += sent;
				totalSent += sent;
			}

			long elapsed = System.currentTimeMillis() - startTime;
			System.out.println("전송 완료: 총 바이트 = " + totalSent + ", 소요 시간(ms) = " + elapsed);
		}
	}
}
