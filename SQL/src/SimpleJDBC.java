// Very basic JDBC example showing loading of JDBC driver, establishing connection
// creating a statement, executing a simple SQL query, and displaying the results.
import java.sql.Connection;
import java.sql.*;

public class SimpleJDBC {
  public static void main(String[] args)
    throws SQLException, ClassNotFoundException {
    // Load the JDBC driver
    Class.forName("com.mysql.cj.jdbc.Driver");

    // Establish a connection
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3312/project3", "root", "conman228");
    System.out.println("Database connected\n");
	
    // Create a statement
    Statement statement = connection.createStatement();

    // Execute a statement
    ResultSet resultSet = statement.executeQuery ("select* from bikes");

    // Iterate through the result set and print the returned results
    while (resultSet.next())
    	System.out.println(resultSet.getString("bikename") + " " + resultSet.getString("cost") + " " + resultSet.getString("country_of_origin"));
    System.out.println();
    System.out.println();
    // Close the connection
    connection.close();
  }
}