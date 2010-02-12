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

public class UserBean {
	private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    
	public UserBean (int id)          { this.id = id;    }
    
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    
}
