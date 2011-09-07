package de.g2fbook.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class DatabaseService {
	private static Connection dbConnection = null;
	private static Statement statement = null;

	public static void init() throws Exception {
		/*
		 * 
		 * TODO: put all to envFile
		 *
		 */
		Class.forName("org.hsqldb.jdbcDriver");
		dbConnection = DriverManager.getConnection(
				"jdbc:hsqldb:mem:DB_g2fbook", "sa", "");
		statement = dbConnection.createStatement();
		execute("CREATE TABLE phoneBook (phonebook_source VARCHAR(50) NOT NULL PRIMARY KEY, phonebook CLOB NOT NULL, insertTime timestamp default current_timestamp)");
		

	}

	public static void finish() throws Exception {

		statement.close();
		dbConnection.close();
	}

	public static void execute(String sql) throws Exception {
		statement.execute(sql);
	}
	public static void query(String sql) throws Exception {
		
		
		  ResultSet rs=statement.executeQuery(sql);
		  while(rs.next()) System.out.println(rs.getString(1)+"--"+rs.getString(2));
		 
	}
	public static void main(String[] args) throws Exception {
		try {
			DatabaseService.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void insertPhoneBook(String phonebook_source,String phonebook) throws Exception{
		 PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO phoneBook (phonebook_source,phonebook) VALUES(?,?)");
        preparedStatement.setString(1, phonebook_source);
        preparedStatement.setString(2, phonebook);
        preparedStatement.execute();
       
     
        preparedStatement.close();
        dbConnection.commit();
	}
}
