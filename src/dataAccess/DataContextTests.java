package dataAccess;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class DataContextTests {

	@Test
	void test() {

		DataContext dataContext = new DataContext();
		try {

			Connection conn = dataContext.getConnection();

			String catalog = conn.getCatalog();
			
			assertEquals("CafeSanchez", catalog);
			assertFalse(conn.isClosed());			
		
		} catch (SQLException e) {

			fail(e.getMessage());
		}
	}
}
