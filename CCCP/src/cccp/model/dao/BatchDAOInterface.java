package cccp.model.dao;

import java.util.List;

import cccp.model.Batch;

public interface  BatchDAOInterface {
	void addBatch(Batch batch, String productId);
    void updateBatch(Batch batch);
    void updateBatchQuantity(Batch batch);
    List<Batch> getBatchesByProductId(String productId);
    int getTotalQuantityForProduct(String productId);		
}
