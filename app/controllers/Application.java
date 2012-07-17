package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.index;

public class Application extends Controller {

	public static Form<User> userForm  = form(User.class);
	
	public static Result index() {
		return Results.ok(index.render(userForm));
	}

	public static Result authenticate() {
		return Results.TODO;
	}

	public static Result signup() {
		return Results.TODO;
	}

}