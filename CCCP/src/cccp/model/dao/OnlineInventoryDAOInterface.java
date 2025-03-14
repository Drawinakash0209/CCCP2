package cccp.model.dao;

public interface OnlineInventoryDAOInterface {
	void addToOnlineInventory(String productId, int quantity);
	
	void reduceOnlineQuantity(String productCode, int quantity);
}
