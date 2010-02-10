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
