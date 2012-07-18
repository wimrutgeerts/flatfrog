package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class User extends Model {

    @Id
    public Long id;
	public String password;
	public String email;
	public String confirmationToken;
	
	public static Model.Finder<Long, User> find = new Model.Finder<Long, User>(Long.class, User.class);

	public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
	
}
