package cccp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cccp.ShelfRestockListener;
import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.strategy.BatchSelectionStrategy;

public class OnlineOrderService implements OnlineOrderServiceInterface {
	 private final BatchDAOInterface batchDAO;
	 private final OnlineInventoryDAOInterface onlineInventoryDAO;
	 private final List<ShelfRestockListener> listeners = new ArrayList<>();
	 
	 
	 public OnlineOrderService(BatchDAOInterface batchDAO, OnlineInventoryDAOInterface onlineInventoryDAO) {
	        this.batchDAO = batchDAO;
	        this.onlineInventoryDAO = onlineInventoryDAO;
	 }
	 
	 public void addRestockListener(ShelfRestockListener listener) {
	        listeners.add(listener);
	 }
	
	 
	 public void allocateStockForOnlineOrder(String productId, int quantity, Date currentDate, BatchSelectionStrategy strategy) {
	        List<Batch> batches = batchDAO.getBatchesByProductId(productId);
	        List<Batch> selectedBatches = strategy.selectBatch(batches, quantity, currentDate);

	        for (Batch batch : selectedBatches) {
	            onlineInventoryDAO.addToOnlineInventory(productId, batch.getQuantity());
	        }
	        
	        for (ShelfRestockListener listener : listeners) {
	            listener.onShelfRestocked(productId);
	        }
	             
	 }
	 
	 public void reduceShelf(String productCode, int quantity) {
			onlineInventoryDAO.reduceOnlineQuantity(productCode, quantity);
			
		}
	 

}
