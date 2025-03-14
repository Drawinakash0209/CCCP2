package cccp.model.dao;

import java.util.Date;
import java.util.List;

import cccp.model.Sale;

public interface SaleDAOInterface {

	void addSale(Sale sale);

    List<Sale> getSalesByDate(Date date);
}
