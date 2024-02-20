package webserver.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

@Slf4j
public class UserListController implements Controller {
	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
	}

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
		int index = httpRequest.getCookie().indexOf(":");
		String cookie = httpRequest.getCookie().substring(index+1).trim();
		log.info("cookie ==== {}", cookie);
		Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
		Collection<User> users = DataBase.findAll();

		// 로그인 상태가 아니면 로그인 페이지로 이동한다.
		if(!Boolean.parseBoolean(cookies.get("logined"))){
			httpResponse.setResponsePath("/user/login.html");
			return;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (User user : users) {
			stringBuilder.append("<tr>\n <th scope=\"row\">").append(user.getUserId()).append("</th> ");
			stringBuilder.append("<td>");
			stringBuilder.append(user.getUserId());
			stringBuilder.append("</td>");
			stringBuilder.append("<td>");
			stringBuilder.append(user.getName());
			stringBuilder.append("</td>");
			stringBuilder.append("<td>");
			stringBuilder.append(user.getEmail());
			stringBuilder.append("</td>");
			stringBuilder.append("<td> <a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
			stringBuilder.append("</tr>");
		}
		httpResponse.setResponsePath("/user/list.html");
		httpResponse.setStringBuilder(stringBuilder);
	}
}
