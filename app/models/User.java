package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.utils.AppException;

import play.data.format.Formats;
import play.db.ebean.Model;

@Entity
public class User extends Model {

    @Id
    public Long id;
	public String password;
	public String email;
	public String confirmationToken;
	
	@Formats.NonEmpty
    public Boolean validated = false;
	
	public static Model.Finder<Long, User> find = new Model.Finder<Long, User>(Long.class, User.class);

	public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
	
    public static User findByConfirmationToken(String token) {
        return find.where().eq("confirmationToken", token).findUnique();
    }
    
    public static boolean confirm(User user) throws AppException {
        if (user == null) {
          return false;
        }

        user.confirmationToken = null;
        user.validated = true;
        user.save();
        return true;
    }
	
}
