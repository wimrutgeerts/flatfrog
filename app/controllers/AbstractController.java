package controllers;

import models.User;
import play.mvc.Controller;

public class AbstractController extends Controller {

	public static User getLoggedInUser(){
		String email = request().username();
		return User.findByEmail(email);
	}
	
}
