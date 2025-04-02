package cccp;

import java.util.List;

import cccp.model.Bill;

public class BillReport {
	private List<Bill> bills;
	
	public BillReport(List<Bill> bills) {
		this.bills = bills;
	}
	
	public List<Bill> getBills() {
        return bills;
    }
	
	public void display() {

		for(Bill bill:bills) {
			System.out.println(bill.toString());
			
		}
	}
	


}
