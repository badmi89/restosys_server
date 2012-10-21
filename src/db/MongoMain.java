package db;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import db.tables.User;

public class MongoMain {
	
	private Mongo mongo;
	private DB database;
	
	public MongoMain(String databaseName) {
		try {
			mongo = new Mongo();
			database = mongo.getDB(databaseName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public User getUserByPasscode(String passcode) {
		DBCollection users = database.getCollection("users");
		users.setObjectClass(User.class);
		
		BasicDBObject query = new BasicDBObject();
		query.put("passcode", passcode);
		
		User user = (User) users.findOne(query);
		return user;
	}
	
}
