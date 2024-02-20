package webserver.servlet;

import java.util.Map;

public class RegisterFormController implements Controller {
	@Override
	public String doPost(Map<String, String> request) {
		return null;
	}

	@Override
	public String doGet(Map<String, String> request) {
		return "/user/form.html";
	}
}
