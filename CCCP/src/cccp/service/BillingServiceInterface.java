package cccp.service;

import java.util.List;

import cccp.Discount;
import cccp.model.Bill;

public interface BillingServiceInterface {
	  void setDiscount(Discount discount);

	  Bill createBill(List<Bill.BillItem> billItems, double cashTendered);

	  Bill createOnlineBill(List<Bill.BillItem> billItems, double cashTendered);

	  Bill.BillItem createBillItem(String productId, int quantity);
}
