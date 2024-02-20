package webserver.servlet;

import java.util.Map;

public class LogInFormController implements Controller{
	@Override
	public String doPost(Map<String, String> request) {
		return null;
	}

	@Override
	public String doGet(Map<String, String> request) {
		return "/user/login.html";
	}
}
