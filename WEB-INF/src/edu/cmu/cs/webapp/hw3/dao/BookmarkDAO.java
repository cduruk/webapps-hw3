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

package edu.cmu.cs.webapp.hw3.dao;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanFactory;
import org.mybeans.factory.BeanFactoryException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.DuplicateKeyException;
import org.mybeans.factory.MatchArg;
import org.mybeans.factory.RollbackException;
import org.mybeans.factory.Transaction;

import edu.cmu.cs.webapp.hw3.databeans.BookmarkBean;
import edu.cmu.cs.webapp.hw3.databeans.UserBean;

public class BookmarkDAO {
	private static BeanFactory<BookmarkBean> factory;
	
	public BookmarkDAO() throws DAOException {
		try {
			BeanTable<BookmarkBean> table = BeanTable.getInstance(BookmarkBean.class,"cduruk_bookmark");
			if (!table.exists()) table.create("id");
			factory = table.getFactory();
		} catch (BeanFactoryException e) {
			throw new DAOException(e);
		}
	}
	
    public static BookmarkBean create(String url, String comment, int userID) throws DAOException {
        try {
			Transaction.begin();

			BookmarkBean newBookmark = factory.create();
			
        	newBookmark.setUrl(url.replaceAll("\\<.*?\\>", ""));
        	newBookmark.setComment(comment.replaceAll("\\<.*?\\>", ""));
        	newBookmark.setUser_id(userID);
        	newBookmark.setClickCount(0);
        	
        	Transaction.commit();
        	return newBookmark;
            
        } catch (RollbackException e) {
            throw new DAOException(e);
        }

    }
	
	public void delete(int id) throws DAOException {
		try {
			factory.delete(id);
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
	public static void updateCount(int id) throws RollbackException{
		Transaction.begin();

		BookmarkBean bookmark = factory.lookup(id);
		Integer current = bookmark.getClickCount();
		bookmark.setClickCount(current+1);
    	
		Transaction.commit();
	}

	public static BookmarkBean[] getItems() throws DAOException {
		try {
			return factory.match();
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
	public static BookmarkBean[] getItemsForUser(int userID) throws DAOException {
		try{
			return factory.match(MatchArg.equals("user_id",userID));
		}
		
		catch(RollbackException e){
			throw new DAOException(e);
		}
	}
	
	public int size() throws DAOException {
		try {
			return factory.getBeanCount();
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
}
