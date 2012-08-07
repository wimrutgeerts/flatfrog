package controllers;

import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class DashBoard {

}
