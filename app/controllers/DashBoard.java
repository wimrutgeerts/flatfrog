package controllers;

import models.User;
import play.api.libs.oauth.ConsumerKey;
import play.api.libs.oauth.OAuth;
import play.api.libs.oauth.OAuthCalculator;
import play.api.libs.oauth.RequestToken;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import views.html.dashboard;

@Security.Authenticated(Secured.class)
public class DashBoard extends Controller {

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
		
		
		return Results.ok(dashboard.render(loggedInUser));
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
