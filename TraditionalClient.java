package sendfile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class TraditionalClient {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 2000;
		String filePath = "sendfile/10mb.bin";

		TraditionalClient client = new TraditionalClient();
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
				Socket socket = new Socket(host, port);
				FileInputStream fileInput = new FileInputStream(file);
				DataOutputStream output = new DataOutputStream(socket.getOutputStream())
		) {
			System.out.println("서버 연결 완료: " + socket.getInetAddress() + ":" + socket.getPort());

			byte[] buffer = new byte[(int) (FileSize.SIZE_1MB.getBytes() * 1.1)];
			int bytesRead;
			long totalBytesSent = 0;
			long startTime = System.currentTimeMillis();

			while ((bytesRead = fileInput.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
				totalBytesSent += bytesRead;
			}

			long elapsed = System.currentTimeMillis() - startTime;
			System.out.println("전송 완료: 총 바이트 = " + totalBytesSent + ", 소요 시간(ms) = " + elapsed);
		}
	}
}
