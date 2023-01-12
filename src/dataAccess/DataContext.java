package dataAccess;

import java.sql.Connection;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class DataContext {
	
	public Connection getConnection() throws SQLServerException {
		
		// SOLVED: Instantiate and return a Connection object that encapsulates an connection to the database
		//         The test is placed in the DataContext.java file
		
		SQLServerDataSource ds = new SQLServerDataSource();  
		ds.setUser("student");  
		ds.setPassword("P@$$w0rd");  
		ds.setServerName("192.168.56.101");  
		ds.setDatabaseName("CafeSanchez");  
		ds.setEncrypt("False");
		
		return ds.getConnection(); 
	}
}
