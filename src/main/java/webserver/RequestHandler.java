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
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.CustomHttpRequestUtil;
import util.HttpRequestUtils;
import webserver.servlet.Controller;

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
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			List<String> headerLines = new ArrayList<>();
			String params = "";
			boolean isHtml = false;

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

			isHtml = isHtml(headerLines);

			for (String headerLine : headerLines) {
				log.info("headerRead = {}", headerLine);
			}

			// 요청 url에서 requestPath와 params를 분리함
			String httpMethod = extreactHttpMethod(headerLines);
			String url = extreactRequestURL(headerLines);
			int index = url.indexOf("?");
			String requestPath = url;

			if (httpMethod.equals(HttpMethod.GET.name()) && (index > 0)) {
				requestPath = url.substring(0, Math.max(index, 0));
				params = url.substring(index + 1);
			}

			// 비즈니스 로직을 처리하고 논리적 뷰를 return 해주는 Controller 추가
			HandlerMapping handlerMapping = new HandlerMapping();
			Controller controller = handlerMapping.controller(requestPath);

			// 요청 값을 httpRequest에 담는다
			HttpRequest httpRequest =
				new HttpRequest(HttpRequestUtils.parseQueryString(params), readCookie(headerLines));
			HttpResponse httpResponse = new HttpResponse(requestPath);

			if(controller != null && httpMethod.equals(HttpMethod.POST.name())){
				controller.doPost(httpRequest, httpResponse);
			}
			if(controller != null && httpMethod.equals(HttpMethod.GET.name())){
				controller.doGet(httpRequest, httpResponse);
			}

			// responseBody를 생성하여 응답한다.
			DataOutputStream dos = new DataOutputStream(out);
			createResponseBody(dos, httpRequest, httpResponse, isHtml);


		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private boolean isHtml(List<String> headerLines){
		return headerLines.stream()
			.anyMatch(headerLine -> headerLine.contains("text/html"));
	}

	private void createResponseBody(DataOutputStream dos, HttpRequest httpRequest, HttpResponse httpResponse, Boolean isHtml) throws IOException {
		byte[] body = null;
		String responsePath = httpResponse.getResponsePath();
		if(responsePath.contains("redirect:/")){
			int index = responsePath.indexOf(":");
			responsePath = responsePath.substring(index+1);
			body = createViewPath(responsePath);
			response300Header(dos, body.length, responsePath, httpRequest, isHtml);
		}else {
			body = createViewPath(responsePath);
			response200Header(dos, body.length, httpRequest, httpResponse, isHtml);
		}
		responseBody(dos, body);
	}

	private String extreactHttpMethod(List<String> lines) {
		if (!lines.isEmpty()) {
			return CustomHttpRequestUtil.parseHttpMethod(lines.get(0), " ");
		}
		return "";
	}

	private byte[] createViewPath(String url) throws IOException {
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

	private String readCookie(List<String> headerLines){
		return headerLines.stream()
			.filter(headerLine -> headerLine.contains("Cookie"))
			.findAny()
			.orElse("");
	}

	private void createCookie(DataOutputStream dos, String cookies){
		try {
			dos.writeBytes("Set-cookie:" + cookies + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response300Header(
		DataOutputStream dos, int lengthOfBodyContent, String requestPath, HttpRequest httpRequest, boolean isHtml) {
		try {
			dos.writeBytes("HTTP/1.1 302 OK \r\n");
			if(!isHtml){
				dos.writeBytes("Content-Type: text/css;charset=utf=8\r\n");
			}else {
				dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			}
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("Location: " + requestPath + "\r\n");
			if(!httpRequest.hasCookies()) {
				createCookie(dos, httpRequest.getCookie());
			}
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(
		DataOutputStream dos, int lengthOfBodyContent, HttpRequest httpRequest, HttpResponse httpResponse, boolean isHtml) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			if(!isHtml){
				dos.writeBytes("Content-Type: text/css;charset=utf=8\r\n");
			}else {
				dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			}
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			if(!httpRequest.hasCookies()) {
				createCookie(dos, httpRequest.getCookie());
			}
			dos.writeBytes("\r\n");
			if(httpResponse.getStringBuilder() != null){
				log.info("response = {}", httpResponse.getStringBuilder());
				dos.writeBytes(String.valueOf(httpResponse.getStringBuilder()));
			}
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
