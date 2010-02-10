package edu.cmu.cs.webapp.hw3;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs.webapp.hw3.dao.BookmarkDAO;
import edu.cmu.cs.webapp.hw3.dao.UserDAO;
import edu.cmu.cs.webapp.hw3.databeans.BookmarkBean;
import edu.cmu.cs.webapp.hw3.databeans.UserBean;
import edu.cmu.cs.webapp.hw3.formbeans.BookmarkForm;
import edu.cmu.cs.webapp.hw3.formbeans.LoginForm;

public class Bookmark extends HttpServlet {
	private BookmarkDAO bookmarkDAO;
	private UserDAO userDAO;

	private FormBeanFactory<BookmarkForm>  bookmarkFormFactory  = FormBeanFactory.getInstance(BookmarkForm.class);
	private FormBeanFactory<LoginForm> loginFormFactory = FormBeanFactory.getInstance(LoginForm.class);

	public void init() throws ServletException {
		String jdbcDriverName = getInitParameter("jdbcDriverName");
		String jdbcURL        = getInitParameter("jdbcURL");

		BeanTable.useJDBC(jdbcDriverName,jdbcURL);

		try {
			userDAO     = new UserDAO();
			bookmarkDAO = new BookmarkDAO();
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			try {
				login(request,response);
			} catch (RollbackException e) {
				e.printStackTrace();
			}
		} else {
			try {
				manageList(request,response);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, RollbackException {
		List<String> errors = new ArrayList<String>();
		LoginForm form = null;

		
		boolean loggedIn = false;
		boolean registered = false;
		
		try {
			form = loginFormFactory.create(request);
			
			if (!form.isPresent()) {
				outputLoginPage(response,form,null, loggedIn, registered);
				return;
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				outputLoginPage(response,form,errors, loggedIn, registered);
				return;
			}

			UserBean user;

			if (form.getButton().equals("Register")) {
				user = userDAO.create(form.getEmail(), form.getPassword());
			} else {
				user = userDAO.lookup(form.getEmail());
				if (user == null) {
					errors.add("No such user");
					outputLoginPage(response,form,errors, loggedIn, registered);
					return;
				}

				if (!form.getPassword().equals(user.getPassword())) {
					errors.add("Incorrect password");
					outputLoginPage(response,form,errors, loggedIn, registered);
					return;
				}
			}

			HttpSession session = request.getSession();
			session.setAttribute("user",user);
			//TODO
			loggedIn = true;
			
			System.out.println("ToDoList: login: user="+user.getEmail());
			manageList(request,response);
		} catch (DAOException e) {
			errors.add(e.getMessage());
			outputLoginPage(response,form,errors, loggedIn, registered);
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			outputLoginPage(response,form,errors, loggedIn, registered);
		}
	}

	private void manageList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
		// Look at the what parameter to see what we're doing to the list
		String what = request.getParameter("what");

		if (what == null) {
			// No change to list requested
			outputToDoList(response);
			return;
		}

		if (what.equals("Add to Top")) {
			processAdd(request,response,true);
			return;
		}

		if (what.equals("Add to Bottom")) {
			processAdd(request,response,false);
			return;
		}

		outputToDoList(response,"No such operation: "+what);
	}

	private void processAdd(HttpServletRequest request, HttpServletResponse response, boolean addToTop) throws ServletException, IOException, DAOException {
		List<String> errors = new ArrayList<String>();

		try {
			BookmarkForm form = bookmarkFormFactory.create(request);
			errors.addAll(form.getValidationErrors());
			if (errors.size() > 0) {
				outputToDoList(response,errors);
				return;
			}

			outputToDoList(response,"Item Added");
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			outputToDoList(response,errors);
		}
	}

	// Methods that generate & output HTML

	private void generateHead(PrintWriter out) {
		out.println("  <head>");
		out.println("    <meta http-equiv=\"cache-control\" content=\"no-cache\">");
		out.println("    <meta http-equiv=\"pragma\" content=\"no-cache\">");
		out.println("    <meta http-equiv=\"expires\" content=\"0\">");
		out.println("    <title>To Do List Login</title>");
		out.println("  </head>");
	}

	private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors, boolean loggedIn, boolean registered) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>To Do List Login</h2>");

		if (errors != null && errors.size() > 0) {
			for (String error : errors) {
				out.println("<p style=\"font-size: large; color: red\">");
				out.println(error);
				out.println("</p>");
			}
		}
		
		if(!loggedIn){
			// Generate an HTML <form> to get data from the user
			out.println("<form method=\"POST\">");
			out.println("    <table/>");
			out.println("        <tr>");
			out.println("            <td style=\"font-size: x-large\">Email:</td>");
			out.println("            <td>");
			out.println("                <input type=\"text\" name=\"email\"");
			out.println("                />");
			out.println("            <td>");
			out.println("        </tr>");
			out.println("        <tr>");
			out.println("            <td style=\"font-size: x-large\">Password:</td>");
			out.println("            <td><input type=\"password\" name=\"password\" /></td>");
			out.println("        </tr>");
			out.println("        <tr>");
			out.println("            <td colspan=\"2\" align=\"center\">");
			out.println("                <input type=\"submit\" name=\"button\" value=\"Login\" />");
			out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
			out.println("            </td>");
			out.println("        </tr>");
			out.println("    </table>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
		}


		
		if(!registered){
			out.println("<form method=\"POST\">");
			out.println("    <table/>");
			out.println("        <tr>");
			out.println("            <td style=\"font-size: x-large\">First Name:</td>");
			out.println("            <td>");
			out.println("                <input type=\"text\" name=\"first\"");
			out.println("                />");
			out.println("            <td>");
			out.println("        </tr>");
			out.println("        <tr>");
			out.println("            <td style=\"font-size: x-large\">Last Name:</td>");
			out.println("            <td><input type=\"password\" name=\"last\" /></td>");
			out.println("        </tr>");
			out.println("        <tr>");
			out.println("            <td style=\"font-size: x-large\">Confirm Password:</td>");
			out.println("            <td><input type=\"password\" name=\"confirm\" /></td>");
			out.println("        </tr>");
			out.println("        <tr>");
			out.println("            <td colspan=\"2\" align=\"center\">");
			out.println("                <input type=\"submit\" name=\"button\" value=\"Login\" />");
			out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
			out.println("            </td>");
			out.println("        </tr>");
			out.println("    </table>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	private void outputToDoList(HttpServletResponse response) throws IOException {
		// Just call the version that takes a List passing an empty List
		List<String> list = new ArrayList<String>();
		outputToDoList(response,list);
	}

	private void outputToDoList(HttpServletResponse response, String message) throws IOException {
		// Just put the message into a List and call the version that takes a List
		List<String> list = new ArrayList<String>();
		list.add(message);
		outputToDoList(response,list);
	}

	private void outputToDoList(HttpServletResponse response, List<String> messages) throws IOException {

		// Get the list of items to display at the end
		BookmarkBean[] beans;
		try {
			beans = BookmarkDAO.getItemsForUser(1);
			
		} catch (DAOException e) {
			// If there's an access error, add the message to our list of messages
			messages.add(e.getMessage());
			beans = new BookmarkBean[0];
		}


		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>Bookmark Manager</h2>");

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
		out.println("        <tr>");
		out.println("            <td style=\"font-size: large\">URL:</td>");
		out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"url\"/></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println("            <td style=\"font-size: large\">Comment:</td>");
		out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"comment\"/></td>");
		out.println("        </tr>");

		out.println("        <tr>");
		out.println("            <td/>");
		out.println("            <td><input type=\"submit\" name=\"action\" value=\"Add\"/></td>");
		out.println("        </tr>");
		out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
		out.println("    </table>");
		out.println("</form>");

		for (String message : messages) {
			out.println("<p style=\"font-size: large; color: red\">");
			out.println(message);
			out.println("</p>");
		}

		out.println("<p style=\"font-size: x-large\">The list now has "+beans.length+" items.</p>");
		out.println("<table>");
		for (int i=0; i<beans.length; i++) {
			out.println("    <tr>");
			out.println("        <td>");
			out.println("            <form method=\"POST\">");
			out.println("                <input type=\"hidden\" name=\"id\" value=\""+beans[i].getId()+"\" />");
			out.println("            </form>");
			out.println("        </td>");
			out.println("        <td valign=\"top\" style=\"font-size: x-large\">&nbsp;"+(i+1)+".&nbsp;</td>");
			out.println("        <td valign=\"top\" style=\"font-size: x-large\">"+beans[i].getUrl()+"</td>");        	
			out.println("        <td valign=\"top\" style=\"font-size: x-large\">"+beans[i].getComment()+"</td>");

			out.println("    </tr>");
		}
		out.println("</table>");

		out.println("</body>");
		out.println("</html>");
	}
}