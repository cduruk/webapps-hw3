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
	
    public String getPassword()  { return password; }
    public String getButton()    { return button; }
	
    public void setPassword(String s)  { password = s.trim(); }
    public void setButton(String s)    { button = s.trim(); }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (password == null || password.length() == 0) errors.add("Password is required");
        if (button == null) errors.add("Button is required");
        if (!button.equals("Login") && !button.equals("Register") && !button.equals("Complete")) errors.add("Invalid button");
        
        if (errors.size() > 0) return errors;
		
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
}
