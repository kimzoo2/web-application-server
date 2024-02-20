package webserver.servlet;

import webserver.HttpRequest;

public interface Controller {

	String doPost(HttpRequest httpRequest);
	String doGet(HttpRequest httpRequest);

}
