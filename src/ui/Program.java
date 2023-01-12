package ui;

import business.OrderHandlingController;
import dataAccess.DataContext;
import dataAccess.OrderDao;
import dataAccess.ProductDao;

public class Program {

	public static void main(String[] args) {
		
		DataContext dataContext = new DataContext();

		OrderDao orderDao = new OrderDao(dataContext);
		ProductDao productDao = new ProductDao(dataContext);
		
		OrderHandlingController orderCtrl = new OrderHandlingController(orderDao, productDao);
		
		MainWindow main = new MainWindow(orderCtrl);
		main.setVisible(true);
	}
}
