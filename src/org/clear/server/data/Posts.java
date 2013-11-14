package org.clear.server.data;

import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Posts {

	public String getUser() {
		return user;
	}

	public String getWhere() {
		return where;
	}

	public String getTime() {
		return time;
	}




	// Every persistent object needs a primary key.
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String user;
	@Persistent
	private String where;
	@Persistent
	private String time;

	
	
	public void save(PersistenceManager pm) {
		// To save an object annotated with @PersistenceCapable,
		// you just need to ask the PersistenceManager to make it
		// persistent (e.g., store it).
		pm.makePersistent(this);
	}

	@JsonIgnore
	public Key getKey() {
		return key;
	}

	@JsonIgnore
	public void setKey(Key key) {
		this.key = key;
	}
	
	public void createPost(final String user, final String where, final String time) {
		this.user = user;
		this.where = where;
		this.time = time;
	}
	
	
	

//	public static Posts byKey(String key, PersistenceManager pm) {
//		Posts friends = null;
//
//		try {
//			// Fetching a specific object by key
//			Key k = KeyFactory.stringToKey(key);
//			friends = pm.getObjectById(Posts.class, k);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return friends;
//	}

	
	public static Posts byUser(String user, PersistenceManager pm) {
		List<Posts> results = null;

		// We can declare a query that finds stored objects with
		// member variables that meet a specific criteria. This is
		// roughly equivalent to looping over a list of Java Friends objects
		// and finding the subset where getUser().equals(u). For security
		// purposes, we declare the type of the parameter "u".
		Query query = pm.newQuery("select from " + Posts.class.getName()
				+ " where user==u");
		query.declareParameters("String u");

		// When the query is executed, we pass in a value for every parameter.
		// In this case, we are binding a value for "u".
		results = (List<Posts>) query.execute(user);

		return (results != null && results.size() == 1) ? results.get(0) : null;
	}

}
