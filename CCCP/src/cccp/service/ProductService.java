package cccp.service;

import cccp.ShelfRestockListener;
import cccp.model.Batch;
import cccp.model.Product;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ProductDAOInterface;

public class ProductService implements ProductServiceInterface, ShelfRestockListener {

	private final ProductDAOInterface productDAO;
	private final BatchDAOInterface batchDAO;

	public ProductService(ProductDAOInterface productDAO,BatchDAOInterface batchDAO ) {
		this.productDAO = productDAO;
        this.batchDAO = batchDAO; 
	}
	
	public void addBatchToProduct(String productId, Batch batch) {
		 batchDAO.addBatch(batch, productId); 
	     recalculateProductQuantity(productId); 
	}
		
	public void recalculateProductQuantity(String productId) {
		Product product = productDAO.getProductById(productId);
		if(product !=null) {
			int totalQuanitity = batchDAO.getTotalQuantityForProduct(productId);
			 productDAO.updateProductQuantity(productId, totalQuanitity);
		}
		
	}

	@Override
	public void onShelfRestocked(String productId) {
		recalculateProductQuantity(productId);
		
	}
		
}
