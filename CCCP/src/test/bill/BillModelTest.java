package test.bill;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import cccp.model.Bill;

class BillModelTest {


	@Test
	void billBuilder_ShouldCreateCompleteBill() {
	    // Arrange
	    Date testDate = new Date();
	    // Use factory method instead of private constructor
	    Bill.BillItem item = Bill.BillItemFactory.createBillItem("P1", "Product 1", 2, 10.0, 20.0);

	    // Act
	    Bill bill = new Bill.BillBuilder()
	        .setBillId(1)
	        .setBillDate(testDate)
	        .setTotalPrice(20.0)
	        .setCashTendered(25.0)
	        .setchangeAmount(5.0)
	        .addBillItem(item)
	        .build();

	    // Assert remains the same
	    assertAll(
	        () -> assertEquals(1, bill.getBillId()),
	        () -> assertEquals(testDate, bill.getBillDate()),
	        () -> assertEquals(20.0, bill.getTotalPrice()),
	        () -> assertEquals(25.0, bill.getCashTendered()),
	        () -> assertEquals(5.0, bill.getChangeAmount()),
	        () -> assertEquals(1, bill.getBillItems().size())
	    );
	}

	@Test
	void billItemFactory_ShouldCreateValidItem() {
	    // Act
	    Bill.BillItem item = Bill.BillItemFactory.createBillItem("P1", "Product 1", 2, 10.0, 20.0);

	    // Assert
	    assertAll(
	        () -> assertEquals("P1", item.getProductCode()),
	        () -> assertEquals("Product 1", item.getproductName()),
	        () -> assertEquals(2, item.getQuantity()),
	        () -> assertEquals(10.0, item.getPrice()),
	        () -> assertEquals(20.0, item.getTotalPrice())
	    );
	}
}
