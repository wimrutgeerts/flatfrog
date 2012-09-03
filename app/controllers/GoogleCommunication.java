package controllers;

import java.util.ArrayList;
import java.util.List;

import models.User;

import org.codehaus.jackson.JsonNode;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Http.Request;

public class GoogleCommunication {
	public static JsonNode getGooglePictures(User loggedInUser){
		
		Promise<Response> responseGooglePictures = 
			WS.url("https://www.googleapis.com/drive/v2/files")
				.setQueryParameter("access_token", loggedInUser.googleAccessToken)
				.setQueryParameter("q", "mimeType = 'image/jpeg'") 
	 			.get();
			
		return Json.toJson(responseGooglePictures.map(new googlePicturePromise()).get());
	}
	
	public static final class googlePicturePromise implements Function<WS.Response, List<UserImage>> {
		@Override
		public List<UserImage> apply(Response arg0) throws Throwable {
			List<UserImage> userImages = new ArrayList<UserImage>();
			JsonNode images = arg0.asJson().get("items");
			for (JsonNode image : images) {
				UserImage userimg = new UserImage();
				userimg.thumbnailUrl = image.get("thumbnailLink").asText();
				userimg.name = image.get("title").asText();
				userImages.add(userimg);
			}
			return userImages;
		}
	}

	public static Oauth2Url getGoogleSignInUrl() {
		return new Oauth2Url(
				"code", 
				"656718308348.apps.googleusercontent.com", 
				"http://localhost:9000/googleOauth2Callback/",
				"https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile", 
				"/profile",
				"auto",
				"offline");
	}
	
	public static void googleCallback(User loggedInUser, Request request){
		loggedInUser.googleAccessCode = request.queryString().get("code")[0];
		loggedInUser.save();
		
		Promise<Response> post = 
			WS.url("http://accounts.google.com/o/oauth2/token")
				.setHeader("Content-Type", "application/x-www-form-urlencoded")
				.post(getPostbody(loggedInUser.googleAccessCode));

		post.map(new GoogleCallbackFunction(loggedInUser));
		
	}
	
	private static String getPostbody(String code) {
		StringBuilder post = new StringBuilder();
		post.append("code=").append(code).append(code+"&");
		post.append("client_id=").append("656718308348.apps.googleusercontent.com").append("&");
		post.append("client_secret=").append("KLVditchXNHW2qP4lr64gyP3").append("&");
		post.append("redirect_uri=").append("http://localhost:9000/googleOauth2Callback/").append("&");
		post.append("grant_type=").append("authorization_code").append("&");
		return post.toString();
	}
	
	private static class GoogleCallbackFunction implements  Function<WS.Response, String> {

		private final User loggedInUser;

		public GoogleCallbackFunction(User loggedInUser) {
			this.loggedInUser = loggedInUser;

		}

		@Override
		public String apply(Response arg0) throws Throwable {
			JsonNode jsonResponse = arg0.asJson();
			jsonResponse.get("access_token");
			jsonResponse.get("token_type");
			jsonResponse.get("expires_in");
			jsonResponse.get("id_token");
			loggedInUser.googleAccessToken = jsonResponse.get("access_token").getTextValue();
			loggedInUser.save();
			return null;
		}
	}
}
