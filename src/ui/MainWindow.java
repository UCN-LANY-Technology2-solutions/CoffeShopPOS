package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import business.OrderHandlingController;
import model.Order;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JList<Order> listActiveOrders;
	private JButton btnNewOrder;
	private JButton btnFinishOrder;

	private OrderHandlingController orderCtrl;

	public MainWindow(OrderHandlingController orderCtrl) {
		super("Orders");
		this.orderCtrl = orderCtrl;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);

		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();

		JPanel panel = new JPanel();
		FlowLayout layout = (FlowLayout) panel.getLayout();
		layout.setAlignment(FlowLayout.RIGHT);

		contentPane.add(panel, BorderLayout.SOUTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		listActiveOrders = new JList<>();
		listActiveOrders.setCellRenderer(new OrderCellRenderer());
		scrollPane.setViewportView(listActiveOrders);

		btnNewOrder = new JButton("New Order");
		btnNewOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				openNewOrderDialog();
			}

		});
		panel.add(btnNewOrder);

		btnFinishOrder = new JButton("Finish Order");
		btnFinishOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				finishSelectedOrder();
			}
		});
		panel.add(btnFinishOrder);

		update();
	}

	private void finishSelectedOrder() {

		Order selectedOrder = listActiveOrders.getSelectedValue();
		if (selectedOrder != null && orderCtrl.setOrderStatusToFinished(selectedOrder)) {

			update();
		}
	}

	private void openNewOrderDialog() {

		NewOrderDialog dialog = new NewOrderDialog(this.orderCtrl);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

		if (dialog.isAccepted()) {
			Order order = dialog.getOrder();

			if (orderCtrl.createActiveOrder(order)) {

				update();
			}
		}
	}

	public void update() {
		List<Order> activeOrders = orderCtrl.getActiveOrders();
		if (activeOrders != null)
			listActiveOrders.setModel(GuiHelpers.mapToListModel(activeOrders));
		listActiveOrders.updateUI();
	}

	// Renderer for active orders list
	public class OrderCellRenderer implements ListCellRenderer<Order> {

		DefaultListCellRenderer renderer = new DefaultListCellRenderer();

		@Override
		public Component getListCellRendererComponent(JList<? extends Order> list, Order order, int index,
				boolean isSelected, boolean cellHasFocus) {

			String cellText = order.getCustomerName();

			return renderer.getListCellRendererComponent(list, cellText, index, isSelected, cellHasFocus);
		}

	}
}
