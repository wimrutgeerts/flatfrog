package models;

import play.db.ebean.Model;

public class User extends Model {

	public String userName;
	public String password;
	public String emailAddress;
	public boolean accept = false;
	
}
