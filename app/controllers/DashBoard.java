package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import models.Event;
import models.EventPicture;
import models.User;
import play.api.libs.oauth.ConsumerKey;
import play.api.libs.oauth.OAuth;
import play.api.libs.oauth.OAuthCalculator;
import play.api.libs.oauth.RequestToken;
import play.data.Form;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import views.html.dashboard;

@Security.Authenticated(Secured.class)
public class DashBoard extends Controller {

	public static Form<EventForm> eventForm  = form(EventForm.class);
	
	public static Result getRegisteredEvents(){
		String email = request().username();
		User loggedInUser = User.findByEmail(email);
		return ok(Json.toJson(loggedInUser.registeredEvents));
	}
	
	public static Result createEvent(){
		Map parameters = request().body().asFormUrlEncoded();
		String email = request().username();
		String eventName = ((String[])parameters.get("eventName"))[0];
		
		Event event = new Event();
		event.eventName = eventName;
		event.pictures.addAll(createDummyEventPictures());
		
		
		User loggedInUser = User.findByEmail(email);
		loggedInUser.registeredEvents.add(event);
		loggedInUser.save();
		
		return ok(dashboard.render(loggedInUser,DashBoard.eventForm));
		
	}
	
	private static List<EventPicture> createDummyEventPictures() {
		List<EventPicture> result= new ArrayList<EventPicture>();

		for(int i = 0; i< 23; i++){
			EventPicture eventPicture = new EventPicture();
			eventPicture.url = "http://www.google.com";
			result.add(eventPicture);
		}
		
		return result;
		
	}

	public static class EventForm{
		
		public String eventName;
		
	}
	
	public static Result googleLogin(String email) {

		Oauth2Url url = new Oauth2Url(
				"code", 
				"656718308348.apps.googleusercontent.com", 
				"http://localhost:9000/googleOauth2Callback/",
				"https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile", 
				"/profile",
				"auto",
				"offline");
	
		return redirect(url.getUrl());
	}

	public static Result googleCallback(){
		User loggedInUser = User.findByEmail(ctx().session().get("email"));
		
		loggedInUser.googleAccessToken = request().queryString().get("code")[0];
		loggedInUser.save();
//		Promise<Response> promise = 
//			WS.url("accounts.google.com/o/oauth2/token HTTP/1.1")
//				.setHeader("Content-Type", "pplication/x-www-form-urlencoded")
//				.post(getPostbody(""));
//		promise.map(new Function<WS.Response, String>() {
//
//			@Override
//			public String apply(Response arg0) throws Throwable {
//				
//				String test = "1";
//				return test;
//			}
//		});
		
		
		return Results.ok(dashboard.render(loggedInUser, eventForm));
	}
	private static String getPostbody(String code) {
		StringBuilder post = new StringBuilder();
		post.append("code=").append(code).append("&");
		post.append("client_id=").append("656718308348.apps.googleusercontent.com").append("&");
		post.append("client_secret=").append("KLVditchXNHW2qP4lr64gyP3").append("&");
		post.append("redirect_uri=").append("http://localhost:9000/googleOauth2PostCallback/").append("&");
		post.append("grant_type=").append("authorization_code").append("&");
		return post.toString();
	}
	/*code=4/P7q7W91a-oMsCeLvIaQm6bTrgtp7&
	client_id=8819981768.apps.googleusercontent.com&
	client_secret={client_secret}&
	redirect_uri=https://oauth2-login-demo.appspot.com/code&
	grant_type=authorization_code
	*/	
}
