package sampleMaps.server;

import java.sql.*;

public class MySQLJDBC {
  public static void main(String args[]){
    System.out.println(
                  "Copyright 2004, R.G.Baldwin");
    try {
      Statement stmt;

      //Register the JDBC driver for MySQL.
      Class.forName("com.mysql.jdbc.Driver");

      //Define URL of database server for
      // database named mysql on the localhost
      // with the default port number 3306.
      String url =
            "jdbc:mysql://localhost:3306/receptionmapper";

      //Get a connection to the database for a
      // user named root with a blank password.
      // This user is the default administrator
      // having full privileges to do anything.
      Connection con =
                     DriverManager.getConnection(
                                 url,"root", "");

      //Display URL and connection information
      System.out.println("URL: " + url);
      System.out.println("Connection: " + con);

      //Get a Statement object
      stmt = con.createStatement();
      
      ResultSet result = stmt.executeQuery("Select * from carrier");
      while(result.next()) {
    	  System.out.println("id = " + result.getInt("id")+ " name = " + result.getString("name"));
      }
//      stmt.execute("CREATE DATABASE JunkDB");
//      stmt.execute("Create table JunkDb.test");
//      stmt.execute("");
      con.close();
    }catch( Exception e ) {
      e.printStackTrace();
    }//end catch
  }//end main
}//end class Jdbc11