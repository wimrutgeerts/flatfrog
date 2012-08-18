package controllers;

public class Oauth2Url {

	private String responseType;
	private String clientId;
	private String redirectUrl;
	private String scope;
	private String state;
	private String approvalPrompt;
	private String accessType;
	
	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	
	public Oauth2Url(String responseType, String clientId, String redirectUrl,
			String scrope, String state, String approvalPrompt, String accessType) {
		this.responseType = responseType;
		this.clientId = clientId;
		this.redirectUrl = redirectUrl;
		this.scope = scrope;
		this.state = state;
		this.approvalPrompt = approvalPrompt;
		this.accessType = accessType;
	}
	
	public String getUrl(){
		StringBuilder url = new StringBuilder(GOOGLE_AUTH_URL);
		url.append("?");
		url.append("scope=").append(scope).append("&");
		url.append("state=").append(state).append("&");
		url.append("redirect_uri=").append(redirectUrl).append("&");
		url.append("response_type=").append(responseType).append("&");
		url.append("client_id=").append(clientId).append("&");
		url.append("access_type=").append(accessType).append("&");
		url.append("approval_prompt=").append(approvalPrompt);
		return url.toString();
	}
	
}
