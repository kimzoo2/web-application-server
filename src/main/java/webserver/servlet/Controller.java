package webserver.servlet;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {

	void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
	void doGet(HttpRequest httpRequest, HttpResponse httpResponse);

}
