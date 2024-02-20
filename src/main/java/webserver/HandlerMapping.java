package webserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import webserver.servlet.Controller;
import webserver.servlet.IndexController;
import webserver.servlet.LogInController;
import webserver.servlet.LogInFormController;
import webserver.servlet.RegisterController;
import webserver.servlet.RegisterFormController;
import webserver.servlet.UserListController;

public class HandlerMapping {

	protected ConcurrentMap<String, Controller> handlerMap = new ConcurrentHashMap<>();

	public HandlerMapping() {
		handlerMap.put("/", new IndexController());
		handlerMap.put("/index.html", new IndexController());
		handlerMap.put("/user/create", new RegisterController());
		handlerMap.put("/user/form.html", new RegisterFormController());
		handlerMap.put("/user/login.html", new LogInFormController());
		handlerMap.put("/user/login", new LogInController());
		handlerMap.put("/user/list", new UserListController());
	}

	public Controller controller(String key){
		return handlerMap.get(key);
	}
}
