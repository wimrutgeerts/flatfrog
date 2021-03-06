package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import models.User;
import models.utils.AppException;
import models.utils.Hash;
import models.utils.Mail;

import org.apache.commons.mail.EmailException;

import play.Configuration;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.confirm;
import views.html.created;
import views.html.dashboard;
import views.html.index;

public class Application extends Controller {

	public static Form<Application.Register> registerForm  = form(Application.Register.class);
	public static Form<Application.Login> loginForm  = form(Application.Login.class);
	
	public static Result index() {
		return Results.ok(index.render(registerForm, loginForm));
	}

	public static Result authenticate() {
		Form<Application.Login> loginForm = form(Application.Login.class).bindFromRequest();
////////
	   User user = new User();
       user.email = loginForm.get().email;
       try {
		user.password = Hash.createPassword(loginForm.get().inputPassword);
	} catch (AppException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		if(User.findByEmail(loginForm.get().email)!= null) user = User.findByEmail(loginForm.get().email);
	
       user.save();
	/////////	
		User loggedInUser = User.authenticate(loginForm.get().email, loginForm.get().inputPassword);
		if (loggedInUser != null){
			session("email", loginForm.get().email);
			return Results.ok(dashboard.render(loggedInUser, DashBoard.eventForm));
		}
		flash("error", Messages.get("error.wrong.username.or.password"));
		return Results.badRequest(index.render(registerForm, loginForm));
	}
	
	public static Result authenticateFromSession() {
		return Results.ok(dashboard.render(User.findByEmail(ctx().session().get("email")), DashBoard.eventForm));
	}

	public static Result alReadySignedUp(){
		flash("error", Messages.get("check.email.for.confirmationmail"));
		 return ok(created.render());
	}
	
	
	public static Result signup() {
		 Form<Application.Register> registerForm = form(Application.Register.class).bindFromRequest();

		 if (registerForm.hasErrors()) {
	            return badRequest(index.render(registerForm, loginForm));
	        }

	        Application.Register register = registerForm.get();
	        Result resultError = checkBeforeSave(registerForm, register.email);

	        if (resultError != null) {
	            return resultError;
	        }

	        try {
	            User user = new User();
	            user.email = register.email;
	            user.password = Hash.createPassword(register.inputPassword);
	            user.confirmationToken = UUID.randomUUID().toString();

	            user.save();
	            sendMailAskForConfirmation(user);

	            return ok(created.render());
	        } catch (EmailException e) {
	            Logger.debug("Signup.save Cannot send email", e);
	            flash("error", Messages.get("error.sending.email"));
	        } catch (Exception e) {
	            Logger.error("Signup.save error", e);
	            flash("error", Messages.get("error.technical"));
	        }
	        return badRequest(index.render(registerForm, loginForm));
	}
	
	private static Result checkBeforeSave(Form<Application.Register> registerForm, String email) {
        // Check unique email
        if (User.findByEmail(email) != null) {
            flash("error", Messages.get("error.email.already.exist"));
            return badRequest(index.render(registerForm, loginForm));
        }

        return null;
     }

	
	 public static class Register {

		@Required
		public String email;
		@Required
		public String inputPassword;
		@Required
		public String accept;

	 }


	 public static class Login {

		@Required
		public String email;
		@Required
		public String inputPassword;

	 }
	 
	  private static void sendMailAskForConfirmation(User user) throws EmailException, MalformedURLException {
	        String subject = Messages.get("mail.confirm.subject");

	        URL url = getSignUpRequest(user);
	        
	        String message =url.toString();

	        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
	        Mail.sendMail(envelop);
	    }

	private static URL getSignUpRequest(User user) throws MalformedURLException {
		String urlString = "http://" + Configuration.root().getString("server.hostname");
		urlString += "/confirm/" + user.confirmationToken;
		URL url = new URL(urlString);
		return url;
	}
	 
	    public static Result confirm(String token) {
	        User user = User.findByConfirmationToken(token);
	        if (user == null) {
	            flash("error", Messages.get("error.unknown.email"));
	            return badRequest(confirm.render());
	        }

	        if (user.validated) {
	            flash("error", Messages.get("error.account.already.validated"));
	            return badRequest(confirm.render());
	        }

	        try {
	            if (User.confirm(user)) {
	                sendMailConfirmation(user);
	                flash("success", Messages.get("account.successfully.validated"));
	                return ok(confirm.render());
	            } else {
	                Logger.debug("Signup.confirm cannot confirm user");
	                flash("error", Messages.get("error.confirm"));
	                return badRequest(confirm.render());
	            }
	        } catch (AppException e) {
	            Logger.error("Cannot signup", e);
	            flash("error", Messages.get("error.technical"));
	        } catch (EmailException e) {
	            Logger.debug("Cannot send email", e);
	            flash("error", Messages.get("error.sending.confirm.email"));
	        }
	        return badRequest(confirm.render());
	    }
	    
	    private static void sendMailConfirmation(User user) throws EmailException {
	        String subject = Messages.get("mail.welcome.subject");
	        String message = Messages.get("mail.welcome.message");
	        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
	        Mail.sendMail(envelop);
	    }
	  
	  
}