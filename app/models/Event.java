package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Event extends Model {
	@Id
	public Long id;
	public String eventName;
	@OneToMany(cascade=CascadeType.PERSIST)
	public List<EventPicture> pictures;
}
