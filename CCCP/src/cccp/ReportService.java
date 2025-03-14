package cccp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cccp.command.ReshelveReportCommand;
import cccp.model.Bill;
import cccp.model.Product;
import cccp.model.Sale;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;
import cccp.model.dao.ShelfDAOInterface;

public class ReportService {
	private BillDAOInterface billDAO;
	private SaleDAOInterface saleDAO;
	private ProductDAOInterface productDAO;
	private ShelfDAOInterface shelfDAO;
	
	public ReportService(BillDAOInterface billDAO) {
	    this.billDAO = billDAO;
	}
	
	public ReportService(SaleDAOInterface saleDAO, ProductDAOInterface productDAO) {
	    this.saleDAO = saleDAO;
	    this.productDAO = productDAO;
	}
	
	public ReportService(ProductDAOInterface productDAO) {
		this.productDAO = productDAO;
	}
	
	public ReportService(ProductDAOInterface productDAO, ShelfDAOInterface shelfDao) {
		this.productDAO = productDAO;
		this.shelfDAO = shelfDao;
	}
	
	public BillReport generateAllBillReports() {
		List<Bill> bills = billDAO.generateAllBillReports();
		return new BillReport(bills);
	}


	public SaleReport generateSalesReport(Date date) {
		List<Sale> sales = saleDAO.getSalesByDate(date);
		Map<String, SaleReport.Summary> salesSummaryMap = new HashMap<>();
		
		for (Sale sale : sales) {
			String productCode = sale.getProductCode();
			SaleReport.Summary summary= salesSummaryMap.getOrDefault(productCode,
					new SaleReport.Summary(productCode, productDAO.getProductById(productCode).getName()));
			summary.addQuantity(sale.getQuantitySold());
			summary.addRevenue(sale.getTotalRevenue());
			salesSummaryMap.put(productCode, summary);
		}
		
		return new SaleReport(salesSummaryMap);
	}
	
	
	public ReorderReport generateReorderReport() {
		List<Product> products = productDAO.getProductBelowReorderLevel();
		return new ReorderReport(products);
	}

	public void generateStockReport() {
		productDAO.generateStockReport();		
	}
	
	public ReshelveReport generateReshelveReport() {
		List<Product> products = productDAO.getAllProducts();
		
		List<ReshelveReport.Item> itemsToReshelve = new ArrayList<>();
		
		for(Product product : products) {
			int shelfQuantity = shelfDAO.getshelfQuantityByProduct(product.getId());
			int restockQuantity = product.getReorderLevel();
			int quantityToReshelve = restockQuantity - shelfQuantity;
			
			if(quantityToReshelve > 0) {
				itemsToReshelve.add(new ReshelveReport.Item(product.getId(), product.getName(), quantityToReshelve));
			}
			
		}
		
		return new ReshelveReport(itemsToReshelve);
	}
	
}
