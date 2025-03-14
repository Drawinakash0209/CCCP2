package cccp.model.dao;

import java.util.List;

import cccp.model.Bill;

public interface BillDAOInterface {

    void addBill(Bill bill);
    int getNextBillId();
    List<Bill> generateAllBillReports();
    List<Bill.BillItem> getBillItemsById(int billId);
}
