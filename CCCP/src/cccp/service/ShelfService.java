package cccp.service;

import cccp.ShelfRestockListener;
import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ShelfDAOInterface;
import cccp.strategy.BatchSelectionStrategy;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class ShelfService implements ShelfServiceInterface {
	private final BatchDAOInterface batchDAO;
	private final ShelfDAOInterface shelfDAO;
	private final List<ShelfRestockListener> listeners = new ArrayList<>();
	
	//Constructor to initialize ShelfService with required DAOs.
	public ShelfService(BatchDAOInterface batchDAO, ShelfDAOInterface shelfDAO) {
        this.batchDAO = batchDAO;
        this.shelfDAO = shelfDAO;
    }
	
	// Adds a listener to be notified when a restock event occurs.
	  public void addRestockListener(ShelfRestockListener listener) {
	        listeners.add(listener);
	    }
	
	  //Restocks the shelf using a batch selection strategy
	public void restockShelf(String productId, int quantity, Date currentDate, BatchSelectionStrategy strategy) {
		List<Batch> batches  = batchDAO.getBatchesByProductId(productId);
		List<Batch> selectedBatches = strategy.selectBatch(batches, quantity, currentDate);
		
		for (Batch batch : selectedBatches) {
			shelfDAO.addToShelf(productId, batch.getQuantity());
		}
		
		// Notify listeners
		for (ShelfRestockListener listener : listeners) {
            listener.onShelfRestocked(productId);
        }
	}

	//Reduces product quantity from the shelf.
	public void reduceShelf(String productCode, int quantity) {
		shelfDAO.reduceShelfQuantity(productCode, quantity);
		
	}

}
