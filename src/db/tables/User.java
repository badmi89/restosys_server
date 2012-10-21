package db.tables;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class User extends BasicDBObject implements DBObject {
	
	public String getId() {
		return getString("id");
	}
	
	public String getFirstname() {
		return getString("fname");
	}
	
	public String getLastname() {
		return getString("lname");
	}
	
	public String getPasscode() {
		return getString("passcode");
	}
	
	public String getRole() {
		return getString("role");
	}
	
	public boolean getInShift() {
		return getInt("inshift") == 1;
	}
	
	public boolean getVisible() {
		return getInt("visible") == 1;
	}
	
	public double getIncome() {
		return getDouble("income");
	}
	
	public Date getStartDate() {
		return getDate("date-start");
	}
	
	public Date getEndDate() {
		return getDate("date-end");
	}
	
	public void setFirstname(String firstname) {
		put("fname", firstname);
	}
	
	public void setLastname(String lastname) {
		put("lname", lastname);
	}
	
	public void setPasscode(String passcode) {
		put("passcode", passcode);
	}
	
	public void setRole(String role) {
		put("role", role);
	}
	
	public void setInShift(boolean inshft) {
		put("inshift", inshft);
	}
	
	public void setVisible(boolean visible) {
		put("visible", visible);
	}
	
	public void setIncome(double income) {
		put("income", income);
	}
	
	public void setStartDate(Date start) {
		put("date-start", start);
	}
	
	public void setEndDate(Date end) {
		put("date-end", end);
	}

}
