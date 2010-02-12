/*
 * Can Duruk (cduruk@)
 * Thu Feb 11 22:45:49 EST 2010
 * 15437 - Web Application Development
 * 
 * Heavily inspired by Jeff Eppinger's ToDoList3 app
 * 
 * You may use, modify and share this code for non-commercial purposes
 * as long a you comply with this license from Creative Commons:
 * Summary of license: http://creativecommons.org/licenses/by-nc-sa/3.0
 * Full Text of License: http://creativecommons.org/licenses/by-nc-sa/3.0/legalcode
 * 
 */

package edu.cmu.cs.webapp.hw3.databeans;
public class BookmarkBean {
	
	private int    id;
	private int	user_id;
	private String url;
	private String comment;
	private int clickCount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

}