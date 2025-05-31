package sendfile;

import java.net.*;
import java.io.*;

public class TraditionalServer {

	public static void main(String args[]) {
		int port = 2000;

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server waiting for client on port " + serverSocket.getLocalPort());

			// 서버는 클라이언트를 계속 수용함
			while (true) {
				try (
						Socket socket = serverSocket.accept();
						InputStream input = socket.getInputStream()
				) {
					System.out.println("Accepted: " + socket.getInetAddress() + ":" + socket.getPort());

					int fileSize = (int) (FileSize.SIZE_10MB.getBytes() * 1.1);
					byte[] buffer = new byte[fileSize];
					long totalBytesRead = 0;
					int bytesRead;

					long start = System.currentTimeMillis();
					while ((bytesRead = input.read(buffer)) != -1) {
						totalBytesRead += bytesRead;
					}
					long elapsed = System.currentTimeMillis() - start;
					System.out.println("전송 완료: 총 바이트 = " + totalBytesRead + ", 소요 시간(ms) = " + elapsed);
				} catch (IOException e) {
					System.err.println("클라이언트 처리 중 오류: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("서버 실행 실패: " + e.getMessage());
		}
	}
}
