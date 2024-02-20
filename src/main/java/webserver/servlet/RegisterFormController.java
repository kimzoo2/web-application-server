package webserver.servlet;

import webserver.HttpRequest;
import webserver.HttpResponse;

public class RegisterFormController implements Controller {
	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

	}

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
		httpResponse.setResponsePath("/user/form.html");
	}
}
