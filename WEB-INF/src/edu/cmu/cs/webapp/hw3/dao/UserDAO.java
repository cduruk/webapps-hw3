/*
 * Copyright (c) 2005-2009 Jeffrey L. Eppinger.  All Rights Reserved.
 *     You may use, modify and share this code for non-commercial purposes
 *     as long a you comply with this license from Creative Commons:
 *     Summary of license: http://creativecommons.org/licenses/by-nc-sa/3.0
 *     Full Text of License: http://creativecommons.org/licenses/by-nc-sa/3.0/legalcode
 *     Specifically, if you distribute your code for non-educational purposes,
 *     you must include this copyright notice in your work.
 *     If you wish to have broader rights, you must contact the copyright holder.
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

import edu.cmu.cs.webapp.hw3.databeans.UserBean;

public class UserDAO {
    private static BeanFactory<UserBean> factory;

    public UserDAO() throws DAOException {
        try {
            BeanTable<UserBean> table = BeanTable.getInstance(UserBean.class,"cduruk_user");
            if (!table.exists()) {
                table.create("id");  
                }
            factory = table.getFactory();
        } catch (BeanFactoryException e) {
            throw new DAOException(e);
        }
    }
    
    public boolean doesUserExist(String email) throws RollbackException{
        	return factory.match(MatchArg.equals("email",email)).length != 0;
    }

    public static UserBean create(String email, String password, String firstName, String lastName) throws DAOException {
        try {
        	
        	if(factory.match(MatchArg.equals("email",email)).length != 0){
        		throw new DAOException("User email already exists: "+email);
        	}
        	
			Transaction.begin();

        	UserBean newUser = factory.create();
        	newUser.setEmail(email.replaceAll("\\<.*?\\>", ""));
        	newUser.setPassword(password.replaceAll("\\<.*?\\>", ""));
        	newUser.setFirstName(firstName.replaceAll("\\<.*?\\>", ""));
        	newUser.setLastName(lastName.replaceAll("\\<.*?\\>", ""));
        	
        	Transaction.commit();
        	return newUser;
            
        } catch (DuplicateKeyException e) {
            throw new DAOException("User email already exists: "+email);
        } catch (RollbackException e) {
            throw new DAOException(e);
        } finally {
			if (Transaction.isActive()) Transaction.rollback();
        }
    }

    public UserBean create(String email) throws RollbackException {
    	Transaction.begin();
    	UserBean newUser = factory.create();
    	newUser.setEmail(email);
    	Transaction.commit();
    	return newUser;
	}
    
    public UserBean create(String email, String password) throws RollbackException {
    	Transaction.begin();
    	UserBean newUser = factory.create();
    	
    	newUser.setEmail(email);
    	newUser.setPassword(password);
    	
    	Transaction.commit();
    	return newUser;
	}

	public UserBean lookup(String email) throws DAOException {
        try {
        	UserBean[] users = factory.match(MatchArg.equals("email",email));
        	if(users.length != 0) return users[0];
        	else return null;
        } catch (RollbackException e) {
            throw new DAOException(e);
        }finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
    }

    public void update(UserBean user) throws DAOException {
        try {
            Transaction.begin();
            String email = user.getEmail();
            UserBean dbUser = factory.lookup(email);
            if (dbUser == null) {
                throw new DAOException("User email does not exist: "+email);
            }
            dbUser.setPassword(user.getPassword());
            Transaction.commit();
        } catch (RollbackException e) {
            throw new DAOException(e);
        } finally {
            if (Transaction.isActive()) Transaction.rollback();
        }
    }
}
