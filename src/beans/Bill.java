package beans;

import java.util.Date;

public class Bill {
	
	private String id;
	private String userId;
	private int printed;
	private double total;
	private Date date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPrinted() {
		return printed;
	}
	public void setPrinted(int printed) {
		this.printed = printed;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
