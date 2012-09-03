package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Event;
import models.EventPicture;
import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import views.html.dashboard;

@Security.Authenticated(Secured.class)
public class DashBoard extends AbstractController {

	public static Form<EventForm> eventForm  = form(EventForm.class);
	
	public static Result getRegisteredEvents(){
		return ok(Json.toJson(getLoggedInUser() .registeredEvents));
	}
	
	public static Result getGooglePictures(){
		return ok(GoogleCommunication.getGooglePictures(getLoggedInUser()));
	}
	
	public static Result createEvent(){
		Map<String, String[]> parameters = request().body().asFormUrlEncoded();
		String eventName = ((String[])parameters.get("eventName"))[0];
		
		Event event = new Event();
		event.eventName = eventName;
		event.pictures.addAll(createDummyEventPictures());
		
		User loggedInUser = getLoggedInUser();
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
	
	public static Result googleLogin() {
		return redirect(GoogleCommunication.getGoogleSignInUrl().getUrl());
	}

	public static Result googleCallback(){
		
		User loggedInUser = getLoggedInUser();
		
		GoogleCommunication.googleCallback(loggedInUser, request());
		
		return Results.ok(dashboard.render(loggedInUser, eventForm));
	}
	
	
}
