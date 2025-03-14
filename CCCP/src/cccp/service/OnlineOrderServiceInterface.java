package cccp.service;

import java.util.Date;

import cccp.ShelfRestockListener;
import cccp.strategy.BatchSelectionStrategy;

public interface OnlineOrderServiceInterface {
	
	void addRestockListener(ShelfRestockListener listener);

    void allocateStockForOnlineOrder(String productId, int quantity, Date currentDate, BatchSelectionStrategy strategy);

    void reduceShelf(String productCode, int quantity);
}
