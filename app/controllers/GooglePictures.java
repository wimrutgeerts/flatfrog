package controllers;

import java.util.ArrayList;

import models.User;
import play.libs.F.Function;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.created;

public class GooglePictures extends Controller{

	public static Result showPictures(String email){
		ArrayList<String> pictures = new ArrayList<String>();
		
		return async(
			      WS.url("http://www.google.com").get().map(
			        new Function<WS.Response, Result>() {
			          public Result apply(WS.Response response) {
			        	  return TODO;
			          }
			        }
			      )
			    );
		
		
	}
	
}
