package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

	public final static String ORDER_STATUS_FINISHED = "Finished";
	public final static String ORDER_STATUS_NEW = "New";
	public final static String ORDER_STATUS_ACTIVE = "Active";

	private int id;
	private Date date;
	private String status;
	private int discount;
	private String customerName;
	private List<Orderline> orderlines;

	public int getId() {

		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public List<Orderline> getOrderlines() {
		return orderlines;
	}

	public void addOrderline(Orderline ol) {
		orderlines.add(ol);
	}

	public double getTotalPrice() {

		double result = 0;
		for (Orderline ol : orderlines) {
			double subtotal = ol.getProduct().getPrice() * ol.getQuantity();
			result += subtotal - (subtotal * (discount / 100f));
		}
		return result;
	}

	public Order() {

		this.orderlines = new ArrayList<Orderline>();
	}
}
