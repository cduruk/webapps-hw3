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

public class BookmarkForm extends FormBean {
	private String url;
	private String comment;
	
	public String getUrl()  { return url; }
	public String getComment () { return comment; }
	
	public void setUrl(String s)  { url = s.trim(); }
	public void setComment(String s)  { comment = s.trim(); }


	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (url == null || url.length() == 0) {
			errors.add("Item URL is required");
		}
		
		if (comment == null || comment.length() == 0) {
			errors.add("Comment is required");
		}

		return errors;
	}
}
