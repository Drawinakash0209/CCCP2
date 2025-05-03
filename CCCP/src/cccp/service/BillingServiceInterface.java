package cccp.service;

import java.util.List;
import java.util.Map;

import cccp.Discount;
import cccp.model.Bill;
import cccp.model.Product;

public interface BillingServiceInterface {
	  void setDiscount(Discount discount);

	  Bill createBill(List<Bill.BillItem> billItems, double cashTendered);

	  Bill createOnlineBill(List<Bill.BillItem> billItems, double cashTendered);
	  
	  Bill.BillItem createBillItem(String productId, int quantity);
	  
	  Bill generateOnlineBill(int customerId, Map<Product, Integer> items);
}
