package webserver;

import static util.IOUtils.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.CustomHttpRequestUtil;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	@Override
	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			List<String> headerLines = new ArrayList<>();
			String params = "";
			Map<String, String> requestArguments = new HashMap<>();

			// 요청 헤더를 읽는다.
			while (true) {
				String line = br.readLine();
				if ((line == null) || line.isEmpty()) {
					Optional<String> contentLength = headerLines.stream()
						.filter(headerLine -> headerLine.contains("Content-Length"))
						.findAny();
					if (contentLength.isPresent()) {
						String[] contentLengthSplit = contentLength.get().split(":");
						params = readData(br, Integer.parseInt(contentLengthSplit[1].trim()));
						log.info("params = {}", params);
					}
					break;
				}
				headerLines.add(line);
			}

			for (String headerLine : headerLines) {
				log.info("headerRead = {}", headerLine);
			}

			// 요청 url에서 requestPath와 params를 분리함
			String httpMethod = extreactHttpMethod(headerLines);
			String url = extreactRequestURL(headerLines);
			int index = url.indexOf("?");
			String requestPath = url;

			//GET 매핑
			if (httpMethod.equals(HttpMethod.GET.name()) && (index > 0)) {
				requestPath = url.substring(0, Math.max(index, 0));
				params = url.substring(index + 1);
			}

			// 요청 값을 객체에 담는다
			requestArguments = HttpRequestUtils.parseQueryString(params);
			User model = createModel(requestArguments);
			log.info("model = {}", model);

			// POST 매핑 (requestBody에서 데이터 옴)
			byte[] body = createBody(requestPath);

			DataOutputStream dos = new DataOutputStream(out);
			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private String extreactHttpMethod(List<String> lines) {
		if (!lines.isEmpty()) {
			return CustomHttpRequestUtil.parseHttpMethod(lines.get(0), " ");
		}
		return "";
	}

	/**
	 * request를 user 객체로 변경한다
	 * @param requestParams
	 * @return User
	 */
	private User createModel(Map<String, String> requestParams) {
		// 하드코딩?
		return new User(
			requestParams.get("userId")
			, requestParams.get("name")
			, requestParams.get("password")
			, requestParams.getOrDefault("email", null)
		);
	}

	private byte[] createBody(String url) throws IOException {
		String pathName = "./webapp";
		log.debug("url : {}", url);
		return Files.readAllBytes(new File(pathName + url).toPath());
	}

	private String extreactRequestURL(List<String> lines) {
		if (!lines.isEmpty()) {
			return CustomHttpRequestUtil.parseURL(lines.get(0), " ");
		}
		return "";
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
