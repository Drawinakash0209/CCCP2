package cccp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;

public class BatchService {
	private final BatchDAOInterface batchDAO;

    public BatchService(BatchDAOInterface batchDAO) {
        this.batchDAO = batchDAO;
    }

    public void addBatch(String productId, String batchId, int quantity, String purchaseDateStr, String expiryDateStr) 
            throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date purchaseDate = dateFormat.parse(purchaseDateStr);
        Date expiryDate = dateFormat.parse(expiryDateStr);

        Batch batch = new Batch(batchId, quantity, purchaseDate, expiryDate);
        batchDAO.addBatch(batch, productId);
    }

    public List<Batch> getBatchesByProductId(String productId) {
        return batchDAO.getBatchesByProductId(productId);
    }

}
