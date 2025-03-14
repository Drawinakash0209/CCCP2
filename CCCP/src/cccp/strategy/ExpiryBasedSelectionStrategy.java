package cccp.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;

public class ExpiryBasedSelectionStrategy implements BatchSelectionStrategy{
	
	  private BatchDAOInterface batchDAO;

	  //DAO inject
	    public ExpiryBasedSelectionStrategy(BatchDAOInterface batchDAO) {
	        this.batchDAO = batchDAO; 
	    }
	    
	@Override
	public List<Batch> selectBatch(List<Batch> batches, int quantity, Date currentDate){
		List<Batch> selectedBatches = new ArrayList<>();
		//Sort by Expiry Date
		batches.sort(Comparator.comparing(Batch::getExpiryDate));
		int remainingQuanity = quantity;
		for (Batch batch : batches) {
				//Skip expired batches
				if(batch.getExpiryDate().before(currentDate)) {
					continue;
				}
				//Determine the quanitity to use from batch
				int usedQuantity = Math.min(remainingQuanity, batch.getQuantity());
				remainingQuanity = remainingQuanity - usedQuantity;	
				// Update the batch quantity 
				batch.setQuantity(batch.getQuantity()- usedQuantity);	
				//save the updated batch to the Database
				batchDAO.updateBatchQuantity(batch);
				
				//Add selected batches 
				selectedBatches.add(new Batch(batch.getBatchcode(), usedQuantity, batch.getPurchaseDate(), batch.getExpiryDate()));
				
				if(remainingQuanity == 0) {
					break; //when the required quanititiy is fulfill 
				}
			}
			if(remainingQuanity > 0) {
				throw new IllegalStateException("Insufficient");
			}
			
			return selectedBatches;
		}
	}

