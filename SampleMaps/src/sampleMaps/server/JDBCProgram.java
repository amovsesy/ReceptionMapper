package sampleMaps.server;
import javax.swing.JOptionPane;
import java.sql.*;
public class JDBCProgram{
	
	static String userid="scott", password = "tiger";
	static String url = "jdbc:odbc:bob";	// String url = "jdbc:mySubprotocol:myDataSource"; ?
	static Statement stmt;
	static Connection con;
	public static void main(String args[]){

	JOptionPane.showMessageDialog(null,"JDBC Programming showing Retrieval of Table Data");
		int choice = -1;
		
		do{
			choice = getChoice();
			if (choice != 0){
				getSelected(choice);
			}
		}
		while ( choice !=  0);
			System.exit(0);
	}

	public static int getChoice()
	{
		String choice;
		int ch;
		choice = JOptionPane.showInputDialog(null,
			"1. Create Employees Table\n"+
			"2. Create Products Table\n"+
			"3. Insert data into Employees Table\n"+
			"4. Insert data into Orders Table\n"+
			"5. Retrieve data for Employees Table\n"+
			"6.  Retrieve data for Orders Table\n"+
			"0. Exit\n\n"+
			"Enter your choice");
		ch = Integer.parseInt(choice);
		return ch;

	}

	public static void getSelected(int choice){
		if(choice==1){
			createEmployees();
		}
		if(choice==2){
			createOrders();
		}
		if(choice==3){
			insertEmployees();
		}
		if(choice==4){
			insertOrders();
		}
		if(choice==5){
			retrieveEmployees();
		}
		if(choice==6){
			retrieveOrders();
		}
	}

	public static Connection getConnection()
	{
				
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	//Class.forName("myDriver.ClassName"); ?

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(url,
				 userid, password);

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		
		return con;
	}
	
	/*CREATE TABLE Employees (
		    Employee_ID INTEGER,
		    Name VARCHAR(30)
		);*/
	

	public static void createEmployees()
	{
		Connection con = getConnection();
		
		String createString;
		createString = "create table Employees (" +
							"Employee_ID INTEGER, " +
							"Name VARCHAR(30))";
		try {
			stmt = con.createStatement();
	   		stmt.executeUpdate(createString);
			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	JOptionPane.showMessageDialog(null,"Employees Table Created");
	}

	/*CREATE TABLE Orders (
		    Prod_ID INTEGER,
		    ProductName VARCHAR(20),
		    Employee_ID INTEGER
		);*/
	
	public static void createOrders()
	{
		Connection con = getConnection();
		
		String createString;
		createString = "create table Orders (" +
						"Prod_ID INTEGER, " +
						"ProductName VARCHAR(20), "+
						"Employee_ID INTEGER )";
		

		try {
			stmt = con.createStatement();
	   		stmt.executeUpdate(createString);

			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	JOptionPane.showMessageDialog(null,"Orders Table Created");
	}
	
	/*Employee_ID 	Name 
	 	6323 		Hemanth 
	 	5768 		Bob 
	 	1234 		Shawn 
	 	5678 		Michaels */
	public static void insertEmployees()
	{
		Connection con = getConnection();
		
		String insertString1, insertString2, insertString3, insertString4;
		insertString1 = "insert into Employees values(6323, 'Hemanth')";
		insertString2 = "insert into Employees values(5768, 'Bob')";
		insertString3 = "insert into Employees values(1234, 'Shawn')";
		insertString4 = "insert into Employees values(5678, 'Michaels')";
		

		try {
			stmt = con.createStatement();
	   		stmt.executeUpdate(insertString1);
	   		stmt.executeUpdate(insertString2);
	   		stmt.executeUpdate(insertString3);
	   		stmt.executeUpdate(insertString4);

			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	JOptionPane.showMessageDialog(null,"Data Inserted into Employees Table");
	}
	
	/*	Prod_ID 	ProductName 	Employee_ID 
	 		543 	Belt 			6323 
	 		432 	Bottle 			1234 
	 		876 	Ring			5678 
 */
	public static void insertOrders()
	{
		Connection con = getConnection();
		
		String insertString1, insertString2, insertString3, insertString4;
		insertString1 = "insert into Orders values(543, 'Belt', 6323)";
		insertString2 = "insert into Orders values(432, 'Bottle', 1234)";
		insertString3 = "insert into Orders values(876, 'Ring', 5678)";
		
	
		try {
			stmt = con.createStatement();
	   		stmt.executeUpdate(insertString1);
	   		stmt.executeUpdate(insertString2);
	   		stmt.executeUpdate(insertString3);
	
			stmt.close();
			con.close();
	
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	JOptionPane.showMessageDialog(null,"Data Inserted into Orders Table");
	}
	
	public static void retrieveEmployees(){
		Connection con = getConnection();
		String result = null;
		String selectString;
		selectString = "select * from Employees";
	    result ="Employee_ID\t\tName\n";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(selectString);
			while (rs.next()) {
			    int id = rs.getInt("Employee_ID");
			    String name = rs.getString("Name");
			    result+=id+"\t\t"+ name+"\n";
			}
			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	JOptionPane.showMessageDialog(null, result);
	}
	
	public static void retrieveOrders(){
		Connection con = getConnection();
		String result = null;
		String selectString;
		selectString = "select * from Orders";
		result ="Prod_ID\t\tProductName\t\tEmployee_ID\n";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(selectString);
			while (rs.next()) {
				int pr_id = rs.getInt("Prod_ID");
				String prodName = rs.getString("ProductName");
			    int id = rs.getInt("Employee_ID");
			    result +=pr_id+"\t\t"+ prodName+"\t\t"+id+"\n";
			}
			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	JOptionPane.showMessageDialog(null, result);
	}


}//End of class