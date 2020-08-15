/* Name: Connor Rinaldo
 Course: CNT 4714 – Spring 2020 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: April 5, 2020
*/

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import java.io.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;

public class WelcomeServlet extends HttpServlet {   
   // process "get" requests from clients
   @Override
   
   
   
protected void doGet( HttpServletRequest request, 
                      HttpServletResponse response ) throws ServletException, IOException  { 
	String command = request.getParameter("textarea"); 
	response.setContentType( "text/html" );
	PrintWriter out = response.getWriter();  
      // send HTML5 page to client
      // start HTML5 document
	
	out.print("<!DOCTYPE html>");
	out.print("<!-- WelcomeServlet.html -->");
	out.print("<html lang='en'>");
	out.print("<head>");
	out.print("<title>Basic Welcome Servlet</title>");
	out.print("<style type='text/css'>");
	out.print("table, td, th{border:2px solid black;}");
	out.print("body{background-color: blue;}");
	out.print(".alert{background-color: #FF0000; color: white; border: 5px solid #000000; max-width: 500px; margin: auto;}"); 
	out.print(".alert.update{background-color: #2196F3;"); 
	out.print("</style>");
	out.print("</head>");
	
	out.print("<body>");
	out.print("<form action = '' method = 'get'>");
	out.print("<p><center>");
	out.print("<b><font color = 'white' size = '6'>");
	out.print("Welcome to the Spring 2020 Porject 4 Enterprise System<br><br>");
	out.print("A Remote Database Management System<br>");
	out.print("<hr>");
	out.print("</font></b>");
	
	out.print("<font color = 'white' size = '4'>");
	out.print("You are connected the the Project 4 Enterprise System Database.<br>");
	out.print("Please enter any valid SQL query or update statement.<br>");
	out.print("If no query/update command is initially provided the Execute button will display all supplier information in the database.<br>");
	out.print("All execution results will appear below.<br><br>");
	out.print("</font>");
	
	out.print("<textarea id = 'textarea' name = 'textarea' rows = '12' cols = '75' style = 'background-color:black; color:green'>"+command+"</textarea><br><br>");
	out.println("<button style = 'background-color: #000000; color: yellow; border-color: white; border: 1px solid'"
			+ " type = 'submit'>Execute Command</button>");
	out.print("</form>");
	out.print("<button style = 'background-color: #000000; color: yellow; border-color: white; border: 1px solid'"
			+ " type = 'reset' onclick = 'document.getElementById('textarea').value = '''>Reset Form</button><br>");
	
	out.print("<hr>");
	out.print("<b><font color = 'white' size = '4'>");
	out.print("Database Results:</b><br><br>");
	
	Connection connection = null;
	try {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3312/project4","root" ,"conman228");
		ResultSet resultSet = connection.createStatement().executeQuery(command);	
		ResultSetMetaData metaData = resultSet.getMetaData();

		int colAmt = metaData.getColumnCount(), rowAmt;
	
		resultSet.last();
		rowAmt = resultSet.getRow();
		
		out.print("<font color = 'black'>");
		out.print("<table style = 'text-align:center;'>");
		out.print("<tr bgcolor = '#FF0000'>");

		for(int i = 0; i < colAmt; i++) {
			out.print("<th>"+metaData.getColumnName(i+1)+"</th>");	
		}
		out.print("</tr>");
		
		for(int i = 0; i < rowAmt; i++) {
			if(i % 2 == 0)
				out.print("<tr bgcolor = '#D3D3D3'>");
			else
				out.print("<tr bgcolor = '#FFFFFF'>");
				
			for(int j = 0; j < colAmt; j++) {
				resultSet.absolute(i+1);
				out.print("<td>"+resultSet.getString(j+1)+"</td>");
			}
			out.print("</tr>");
		}
		out.print("</table>");
		out.print("</font>");
		
	} catch(Exception e1) {//CATCHES ERROS 
		try {
			int result = 0;
			if(command.contains("shipments")) {
				
				ResultSet tables = connection.createStatement().executeQuery("show tables;");
				int blr = 0;
				
				while(tables.next()) 
					if(tables.getString(1).equals("temp_shipments")) 
						connection.createStatement().executeUpdate("DROP TABLE temp_shipments");
					
				connection.createStatement().executeUpdate("create table temp_shipments LIKE shipments;");
				connection.createStatement().executeUpdate("insert into temp_shipments select*from shipments;");
				
				if(command.contains("update")) {
			
				result = connection.createStatement().executeUpdate(command);
				blr = connection.createStatement().executeUpdate("update suppliers set status = status + 5 " + 
						"where snum in (" + 
						"select distinct snum " + 
						"from shipments inner join temp_shipments " + 
						"using (snum, pnum,jnum) " + 
						"where temp_shipments.quantity < 100 AND shipments.quantity >= 100);");
				}else if(command.contains("insert")){
					result = connection.createStatement().executeUpdate(command);
					blr = connection.createStatement().executeUpdate("update suppliers set status = status + 5 " + 
							"where snum in (" + 
							"select distinct snum from shipments " + 
							"left join temp_shipments " + 
							"using(snum) " + 
							"where temp_shipments.snum is null and shipments.quantity > 100);");
				}
				
				out.print("<div class = 'alert update'>");
				out.print("The statement executed successfully.<br>");
				out.print(result + " row(s) affected.<br>");
				out.print("Business Logic Detected! - Updating supplier Status<br>");
				out.print("Business Logic updated " + blr + " supplier status marks.");
				out.print("</div>");
				
					
				
			}else{
				result = connection.createStatement().executeUpdate(command);
				out.print("<div class = 'alert update'>");
				out.print("The statement executed successfully.<br>");
				out.print(result + " row(s) affected.");
				out.print("</div>");
			}
			
		}catch(Exception e2){//CATCHES ERROS 
			out.print("<div class = 'alert'>");
			out.print("Error executing the SQL statement:<br>");
			out.print(e2.getMessage());
			out.print("</div>");
		}
	}
	
	out.print("</font></b>");
	out.print("</center></p>");
	out.print("</body>");
	out.print("</html>");
   } 
} 
