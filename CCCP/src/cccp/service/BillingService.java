package cccp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cccp.Discount;
import cccp.model.Bill;
import cccp.model.Product;
import cccp.model.Sale;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;


public class BillingService implements BillingServiceInterface {
	
	private final BillDAOInterface billDAO;
	private final ProductDAOInterface productDAO;
	private ShelfServiceInterface shelfService;
	private SaleDAOInterface saleDAO;
	private Discount discount;
	private OnlineOrderServiceInterface onlineOrderService;
 
	public BillingService(BillDAOInterface billDAO,ProductDAOInterface productDAO,ShelfServiceInterface shelfService, SaleDAOInterface saleDAO) {
		this.billDAO = billDAO;
		this.productDAO = productDAO;
		this.shelfService = shelfService;
		this.saleDAO = saleDAO;
	}
	
	public BillingService(BillDAOInterface  billDAO,ProductDAOInterface productDAO,OnlineOrderServiceInterface onlineOrderService, SaleDAOInterface saleDAO) {
		this.billDAO = billDAO;
		this.productDAO = productDAO;
		this.onlineOrderService = onlineOrderService;
		this.saleDAO = saleDAO;
	}
		
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	
	
	public Bill createBill(List<Bill.BillItem> billItems, double cashTendered) {
	    double totalPrice = billItems.stream().mapToDouble(Bill.BillItem::getTotalPrice).sum();
	    
	    
	    double discountAmount = 0.0;
	    
	    if (discount != null) {
            discountAmount = totalPrice - discount.apply(totalPrice); // Calculate discount amount
            totalPrice = discount.apply(totalPrice); // Apply discount to total price
        }
	    
	    double changeAmount = cashTendered - totalPrice;

	    int billId = billDAO.getNextBillId();

	    Bill bill = new Bill.BillBuilder()
	            .setBillId(billId)
	            .setBillDate(new Date())
	            .setTotalPrice(totalPrice)
	            .setCashTendered(cashTendered)
	            .setchangeAmount(changeAmount)
	            .setDiscount(discountAmount)
	            .build();

	    Bill.BillBuilder billBuilder = new Bill.BillBuilder()
	            .setBillId(bill.getBillId())
	            .setBillDate(bill.getBillDate())
	            .setTotalPrice(bill.getTotalPrice())
	            .setCashTendered(bill.getCashTendered())
	            .setchangeAmount(bill.getChangeAmount())
	    		.setDiscount(discountAmount);
	    
	    for (Bill.BillItem billItem : billItems) {
	        billBuilder.addBillItem(billItem);
	    }
	    
	    bill = billBuilder.build();
	    billDAO.addBill(bill);
	    
	    // Reduce item quantity in shelf after generating bill
	    for (Bill.BillItem billItem : billItems) {
	    	shelfService.reduceShelf(billItem.getProductCode(), billItem.getQuantity());
	    	Sale sale = new Sale.SaleBuilder()
	    			.setSaleId(0)
	    			.setProductCode(billItem.getProductCode())
	    			.setQuantitySold(billItem.getQuantity())
	    			.setTotalRevenue(billItem.getTotalPrice())
	    			.setSalesDate(new Date())
	    			.setSaleType("INSTORE")
	    			.build();
	    	saleDAO.addSale(sale);
	    }
	    
	    return bill;
	}
	
	
	public Bill createOnlineBill(List<Bill.BillItem> billItems, double cashTendered) {
	    double totalPrice = billItems.stream().mapToDouble(Bill.BillItem::getTotalPrice).sum();
	    
	    if (discount != null) {
	    	totalPrice = discount.apply(totalPrice);
	    }
	    
	    double changeAmount = cashTendered - totalPrice;

	    int billId = billDAO.getNextBillId();

	    Bill bill = new Bill.BillBuilder()
	            .setBillId(billId)
	            .setBillDate(new Date())
	            .setTotalPrice(totalPrice)
	            .setCashTendered(cashTendered)
	            .setchangeAmount(changeAmount)
	            .build();

	    Bill.BillBuilder billBuilder = new Bill.BillBuilder()
	            .setBillId(bill.getBillId())
	            .setBillDate(bill.getBillDate())
	            .setTotalPrice(bill.getTotalPrice())
	            .setCashTendered(bill.getCashTendered())
	            .setchangeAmount(bill.getChangeAmount());

	    for (Bill.BillItem billItem : billItems) {
	        billBuilder.addBillItem(billItem);
	    }
	    
	    bill = billBuilder.build();
	    billDAO.addBill(bill);
	    
	    // Reduce item quantity in online inventory after generating bill
	    for (Bill.BillItem billItem : billItems) {
	    	onlineOrderService.reduceShelf(billItem.getProductCode(), billItem.getQuantity());
	    	Sale sale = new Sale.SaleBuilder()
	    			.setSaleId(0)
	    			.setProductCode(billItem.getProductCode())
	    			.setQuantitySold(billItem.getQuantity())
	    			.setTotalRevenue(billItem.getTotalPrice())
	    			.setSalesDate(new Date())
	    			.setSaleType("ONLINE")
	    			.build();
	    	saleDAO.addSale(sale);
	    }
	    
	    return bill;
	}
	
	  public Bill generateOnlineBill(int customerId, Map<Product, Integer> items) {
	        List<Bill.BillItem> billItems = new ArrayList<>();
	        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
	            Product product = entry.getKey();
	            int quantity = entry.getValue();
	            Bill.BillItem billItem = createBillItem(product.getId(), quantity); // Assuming Product has getId()
	            billItems.add(billItem);
	        }

	        // Assuming cashTendered equals total price for simplicity (no change)
	        double totalPrice = billItems.stream().mapToDouble(Bill.BillItem::getTotalPrice).sum();
	        return createOnlineBill(billItems, totalPrice);
	    }

	
	public Bill.BillItem createBillItem(String productId, int quantity){
		
		Product product = productDAO.getProductById(productId);
		double totalPrice = product.getPrice() * quantity;
		
		return Bill.BillItemFactory.createBillItem(productId, product.getName(), quantity, product.getPrice(), totalPrice);
	}
	
	
}
