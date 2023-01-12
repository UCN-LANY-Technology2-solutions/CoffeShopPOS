package dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDao {
	
	private DataContext dataContext;
	
	public ProductDao(DataContext dataContext) {

		this.dataContext = dataContext;
	}

	public List<Product> getAll(){
		
		// SOLVED: Implement call to database that gets all products from the Products table
		// QUESTION: Is it necessary to use a transaction for that? (No)
		
		String sql = "SELECT * FROM Products";
		ArrayList<Product> result = new ArrayList<>();
		
		try {
			Connection conn = dataContext.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
			
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String description = rs.getString(3);
				double price = rs.getDouble(4);
				
				Product p = new Product(id, name, description, price);

				result.add(p);				
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}		
		return result;
	}
}
