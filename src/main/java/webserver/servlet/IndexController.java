package webserver.servlet;

import webserver.HttpRequest;

public class IndexController implements Controller {
	@Override
	public String doPost(HttpRequest httpRequest) {
		return null;
	}

	@Override
	public String doGet(HttpRequest httpRequest) {
		return "/index.html";
	}
}
