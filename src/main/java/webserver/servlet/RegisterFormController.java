package webserver.servlet;

import webserver.HttpRequest;

public class RegisterFormController implements Controller {
	@Override
	public String doPost(HttpRequest httpRequest) {
		return null;
	}

	@Override
	public String doGet(HttpRequest httpRequest) {
		return "/user/form.html";
	}
}
