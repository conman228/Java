import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;

<%
try{
Connection connection;
connection = DriverManager.getConnection("jdbc:mysql://localhost:3312/","root" ,"conman228");
}catch(Eception e){
System.out.println("ooops");
}
%>

<!DOCTYPE html">
<!-- WelcomeServlet.html -->
<html lang="en">
<head>
   <title>Basic Welcome Servlet</title>
   <style type="text/css">
	    body{background-color: blue;}
	</style>
</head>
<body>
   <p><center>
        <b><font color = "white" size = "6">
             Welcome to the Spring 2020 Porject 4 Enterprise System<br><br>
             A Remote Database Management System<br>
            <hr>
        </font></b>

        <font color = "white" size = "4">
            You are connected the the Project 4 Enterprise System Database.<br>
            Please enter any valid SQL query or update statement.<br>
            If no query/update command is initially provided the Execute button will display all supplier information in the database.<br>
            All execution results will appear below.<br><br>
        </font>

        <textarea id = "textarea" rows = "12" cols = "75" style = "background-color:black; color:green">select * from suppliers</textarea><br><br>
        <button type = "button"/>
        <%
        try{
            ResultSet resultSet = connection.createStatement().executeQuery(request.getParameter("textarea"));
            ResultSetMetaData metaData = resultSet.getMetaData();
					
            String[] colName;
            String[][] values;
            int colAmt = metaData.getColumnCount(), rowAmt;
                            
            resultSet.last();
            rowAmt = resultSet.getRow();
            colName = new String[colAmt];
            values = new String[rowAmt][colAmt];

            }catch(Exception e1){
                try{
                    int result = connection.createStatement().executeUpdate(request.getParameter("textarea"));
				    
                }catch(Exception e2){
                    //show e2 error
                }
            }
        %>
        <button type = "button" onclick = "document.getElementById('textarea').value = ''">Reset Form</button><br>
        <hr>

        <b><font color = "white" size = "4">
            Database Results
        </font></b>
        
    </center></p>
    </form>
</body>
</html>