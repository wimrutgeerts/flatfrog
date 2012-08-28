package controllers;

import models.Event;
import models.User;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller{

//	public static Result createNewEvent(String email){
//		Form<EventForm> registerForm = form(EventForm.class).bindFromRequest();
//
//		Event event = new Event();
//		event.eventName = registerForm.get().eventName;
//
//		User eventOwner = User.findByEmail(email);
//		eventOwner.registeredEvents.add(event);
//		
//		return ok(routes.addPicturesToEvent.render(event));
//		
//	}
	
	public static class EventForm{
		@Required
		public String eventName;
	}
	
}
