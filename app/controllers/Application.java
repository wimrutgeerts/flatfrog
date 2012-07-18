package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.mail.EmailException;

import models.User;
import models.utils.Hash;
import models.utils.Mail;
import play.Configuration;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.index;
import views.html.created;

public class Application extends Controller {

	public static Form<Application.Register> registerForm  = form(Application.Register.class);
	
	public static Result index() {
		return Results.ok(index.render(registerForm));
	}

	public static Result authenticate() {
		return Results.TODO;
	}

	public static Result signup() {
		 Form<Application.Register> registerForm = form(Application.Register.class).bindFromRequest();

	        if (registerForm.hasErrors()) {
	            return badRequest(index.render(registerForm));
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
	        return badRequest(index.render(registerForm));
	}
	
	private static Result checkBeforeSave(Form<Application.Register> registerForm, String email) {
        // Check unique email
        if (User.findByEmail(email) != null) {
            flash("error", Messages.get("error.email.already.exist"));
            return badRequest(index.render(registerForm));
        }

        return null;
     }

	
	 public static class Register {

	        @Constraints.Required
	        public String email;

	        @Constraints.Required
	        public String inputPassword;
	        
	        @Constraints.Required
	        public Boolean accept;

	        /**
	         * Validate the authentication.
	         *
	         * @return null if validation ok, string with details otherwise
	         */
	        public String validate() {
	            if (isBlank(email)) {
	                return "Email is required";
	            }

	            if (isBlank(inputPassword)) {
	                return "Password is required";
	            }
	            
	            if (! accept) {
	            	return "please accept terms and conditions";
	            }

	            return null;
	        }

	        private boolean isBlank(String input) {
	            return input == null || input.isEmpty() || input.trim().isEmpty();
	        }
	    }

	  private static void sendMailAskForConfirmation(User user) throws EmailException, MalformedURLException {
	        String subject = Messages.get("mail.confirm.subject");

	        String urlString = "http://" + Configuration.root().getString("server.hostname");
	        urlString += "/confirm/" + user.confirmationToken;
	        URL url = new URL(urlString); // validate the URL, will throw an exception if bad.
	        String message = Messages.get("mail.confirm.message", url.toString());

	        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
	        Mail.sendMail(envelop);
	    }
	 

}