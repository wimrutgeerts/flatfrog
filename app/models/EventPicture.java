package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class EventPicture extends Model {

	@Id
	public Long id;
	public String url;
	
}
