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

package edu.cmu.cs.webapp.hw3.formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class LoginForm extends FormBean{
    private String email;
    private String first;
    private String last;
    private String password;
    private String secret;
    private String button;
    private String confirm;
	
    public String getPassword()  { return password; }
    public String getButton()    { return button; }
	
    public void setPassword(String s)  { password = s.trim(); }
    public void setButton(String s)    { button = s.trim(); }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (secret == null && (password == null || password.length() == 0)) errors.add("Password is required");
        if (button == null) errors.add("Button is required");
        if (!button.equals("Login") && !button.equals("Register") && !button.equals("Complete")) errors.add("Invalid button");
        
        if (errors.size() > 0) return errors;
		
        return errors;
    }
    
    public List<String> getCompleteErrors(){
    	List<String> errors = new ArrayList<String>();
    	
        if ((first == null || first.length() == 0)) errors.add("First Name is required");
        if ((last == null || last.length() == 0)) errors.add("Last Name is required");

    	
    	if(!confirm.equals(secret)){
    		errors.add("Two passwords do not match");
    	}
    	
    	return errors;
    }
    
    public List<String> getRegisterErrors(){
    	List<String> errors = new ArrayList<String>();

        if ((email == null || email.length() == 0)) errors.add("Email is required");
        if ((password == null || password.length() == 0)) errors.add("Password is required");
        
		if(email.indexOf("@") <= 0 || email.indexOf("@") == email.length()-1){
			errors.add("You must enter a valid email");
		}
    	
    	return errors;
    }
    
    public List<String> getLoginErrors(){
    	List<String> errors = new ArrayList<String>();
    	
        if ((email == null || email.length() == 0)) errors.add("Email is required");
        if ((password == null || password.length() == 0)) errors.add("Password is required");
    	
    	return errors;
    }

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getSecret() {
		return secret;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getLast() {
		return last;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getFirst() {
		return first;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getConfirm() {
		return confirm;
	}
}
