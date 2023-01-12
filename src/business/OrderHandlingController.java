package business;

import java.util.List;
import java.util.stream.Collectors;

import dataAccess.OrderDao;
import dataAccess.ProductDao;
import model.Order;
import model.Product;

public class OrderHandlingController {

	private OrderDao orderDao;
	private ProductDao productDao;

	public boolean createActiveOrder(Order order) {
		
		Order createdOrder = orderDao.createOrder(order);
		
		return createdOrder.getStatus().equals(Order.ORDER_STATUS_ACTIVE);
	}

	public boolean setOrderStatusToFinished(Order selectedOrder) {
		
		selectedOrder.setStatus(Order.ORDER_STATUS_FINISHED);
		Order updatedOrder = orderDao.updateOrder(selectedOrder);
		
		return updatedOrder.getStatus().equals(Order.ORDER_STATUS_FINISHED);
	}

	public List<Product> getProductsForSale() {

		return  productDao.getAll();
	}

	public List<Order> getActiveOrders() {
		
		List<Order> orders = orderDao.getAll();
		
		if(orders == null)
			return null; 
		
		return orders.stream().filter(O -> O.getStatus().equals(Order.ORDER_STATUS_ACTIVE)).collect(Collectors.toList());		
	}
	
	public OrderHandlingController(OrderDao orderDao, ProductDao productDao) {
		
		this.productDao = productDao;
		this.orderDao = orderDao;
	}
}
