package dataAccess;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.Orderline;
import model.Product;

public class OrderDao {

	private DataContext dataContext;

	public OrderDao(DataContext dataContext) {

		this.dataContext = dataContext;
	}

	public List<Order> getAll() {

		// SOLVED: Implement call to database that gets all orders from the Orders table

		List<Order> result = new ArrayList<>();
		try {

			Connection conn = dataContext.getConnection();

			String sql = "SELECT Id, CustomerName, Discount, Status, Date FROM Orders ";

			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				// mapping order
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setCustomerName(rs.getString(2));
				order.setDiscount(rs.getInt(3));
				order.setStatus(rs.getString(4));
				order.setDate(rs.getDate(5));

				addOrderlines(conn, order);

				result.add(order);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return result;
	}

	public Order getSingle(String customerName) {

		// TODO: Implement call to database that gets a single order from the Orders
		// table
		// QUESTION: Which property in the order can be used to identify a single order
		// and how can you send that to this method?
		// ANSWER: adding customerName parameter to the method signature

		try {

			Connection conn = dataContext.getConnection();

			String sqlSelectOrders = "SELECT Id, CustomerName, Discount, Status, Date FROM Orders WHERE CustomerName = ? ";

			PreparedStatement statement = conn.prepareStatement(sqlSelectOrders);
			statement.setString(1, customerName);

			ResultSet rsOrders = statement.executeQuery();

			if (rsOrders.next()) {
				// mapping order
				Order order = new Order();
				order.setId(rsOrders.getInt(1));
				order.setCustomerName(rsOrders.getString(2));
				order.setDiscount(rsOrders.getInt(3));
				order.setStatus(rsOrders.getString(4));
				order.setDate(rsOrders.getDate(5));

				addOrderlines(conn, order);

				return order;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	private void addOrderlines(Connection conn, Order order) throws SQLException {

		String sqlSelectOrderlines = "SELECT Products.Id, Products.Name, Products.Description, Products.Price, Quantity "
				+ "FROM Orderlines JOIN Products ON Products.Id = Orderlines.ProductId " + "WHERE OrderId = ? ";

		// get orderlines
		PreparedStatement statementSelectOrderlines = conn.prepareStatement(sqlSelectOrderlines);

		statementSelectOrderlines.setInt(1, order.getId());

		ResultSet rsOrderlines = statementSelectOrderlines.executeQuery();

		while (rsOrderlines.next()) {
			// mapping product
			Product p = new Product(rsOrderlines.getInt(1), rsOrderlines.getString(2), rsOrderlines.getString(3),
					rsOrderlines.getInt(4));
			// mapping orderlines
			Orderline ol = new Orderline();
			ol.setProduct(p);
			ol.setQuantity(rsOrderlines.getInt(5));
			order.addOrderline(ol);
		}
	}

	public Order createOrder(Order order) {

		// TODO: Implement call to database that creates an order in the Orders table
		// NOTE: As you might have noticed, there isn't any OrderlineDao class in this
		// solution, so you must handle orderlines here as well.

		try {

			Connection conn = dataContext.getConnection();
			conn.setAutoCommit(false);

			String sqlInsertOrder = "INSERT INTO Orders (Date, Status, Discount, CustomerName) VALUES (?, ?, ?, ?)";
			PreparedStatement statementInsertOrder = conn.prepareStatement(sqlInsertOrder,
					Statement.RETURN_GENERATED_KEYS);
			statementInsertOrder.setDate(1, new Date(System.currentTimeMillis()));
			statementInsertOrder.setString(2, Order.ORDER_STATUS_ACTIVE);
			statementInsertOrder.setInt(3, order.getDiscount());
			statementInsertOrder.setString(4, order.getCustomerName());

			int rowsInserted = statementInsertOrder.executeUpdate();

			if (rowsInserted == 1) {
				ResultSet rs = statementInsertOrder.getGeneratedKeys();
				if (rs.next()) {
					order.setId(rs.getInt(1));
					order.setStatus(Order.ORDER_STATUS_ACTIVE);
					
					String sqlInsertOrderline = "INSERT INTO Orderlines (OrderId, ProductId, Quantity) VALUES (?, ?, ?)";
					
					for (Orderline ol : order.getOrderlines()) {
						PreparedStatement statementInsertOrderline = conn.prepareStatement(sqlInsertOrderline);
						statementInsertOrderline.setInt(1, order.getId());
						statementInsertOrderline.setInt(2, ol.getProduct().getId());
						statementInsertOrderline.setInt(3, ol.getQuantity());
						
						rowsInserted = statementInsertOrderline.executeUpdate();
						
						if(rowsInserted == 0) {
							conn.rollback();
							return null;
						}
					}

					conn.commit();

					return order;
				}
			}

			conn.rollback();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public Order updateOrder(Order order) {

		// SOLVED: Implement call to database that updates an order in the Orders table
		// NOTE: remember the orderlines :-)

		try {

			Connection conn = dataContext.getConnection();
			conn.setAutoCommit(false);

			String sqlUpdateOrder = "UPDATE Orders SET Status = ?, Discount = ? WHERE Id = ? ";
			// update order
			PreparedStatement statement = conn.prepareStatement(sqlUpdateOrder);
			statement.setString(1, order.getStatus());
			statement.setInt(2, order.getDiscount());
			statement.setInt(3, order.getId());
			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated == 1) {
				// clear orderlines
				String sqlDeleteOrderlines = "DELETE FROM Orderlines WHERE OrderId = ?";
				PreparedStatement statementDeleteOrderlines = conn.prepareStatement(sqlDeleteOrderlines);
				statementDeleteOrderlines.setInt(1, order.getId());
				statementDeleteOrderlines.execute();

				// add orderlines
				String sqlInsertOrderlines = "INSERT INTO Orderlines (OrderId, ProductId, Quantity) VALUES (?, ?, ?)";

				for (Orderline ol : order.getOrderlines()) {
					PreparedStatement statementInsertOrderline = conn.prepareStatement(sqlInsertOrderlines);
					statementInsertOrderline.setInt(1, order.getId());
					statementInsertOrderline.setInt(2, ol.getProduct().getId());
					statementInsertOrderline.setInt(3, ol.getQuantity());
					statementInsertOrderline.execute();
				}

				conn.commit();

				return order;

			} else {

				conn.rollback();
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}
}
