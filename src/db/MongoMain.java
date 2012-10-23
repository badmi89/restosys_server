package db;

import java.net.UnknownHostException;
import java.util.List;

import org.json.simple.JSONArray;

import beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

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
	
	public BasicDBObject getUserByPasscode(String passcode) {
		DBCollection users = database.getCollection("users");
		
		BasicDBObject query = new BasicDBObject();
		query.put("passcode", passcode);
		
		return (BasicDBObject) users.findOne(query);
	}
	
	public JSONArray getUserBills(String userID) {
		DBCollection bills = database.getCollection("bills");
		BasicDBObject query = new BasicDBObject();
		query.put("user-id", userID);
		
		JSONArray result = new JSONArray();
		DBCursor cursor = bills.find(query);
		for (DBObject dbObject : cursor) {
			result.add(dbObject);
		}
		
		return result;
	}
	
}
