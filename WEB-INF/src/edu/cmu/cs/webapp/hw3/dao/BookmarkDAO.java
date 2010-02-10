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
			BeanTable<BookmarkBean> table = BeanTable.getInstance(BookmarkBean.class,"bookmark");
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
			
        	newBookmark.setUrl(url);
        	newBookmark.setComment(comment);
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
