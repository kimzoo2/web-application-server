package webserver.servlet;

import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserListController implements Controller {
	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
	}

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
		String cookie = httpRequest.getCookie();
		Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
		String userId = cookies.get("userId");
		User user = DataBase.findUserById(userId);

		httpResponse.setResponsePath("/user/list.html");
		if(user == null){
			httpResponse.setResponsePath("/user/login.html");
		}
	}
}
