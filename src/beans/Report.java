package beans;

import java.util.Date;
import java.util.List;

public class Report {
	
	private String waiterID;
	private String waiterName;
	private Date date;
	private double total;
	private List<Article> articles;
	
	public String getWaiterID() {
		return waiterID;
	}
	public void setWaiterID(String waiterID) {
		this.waiterID = waiterID;
	}
	public String getWaiterName() {
		return waiterName;
	}
	public void setWaiterName(String waiterName) {
		this.waiterName = waiterName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
	

}
