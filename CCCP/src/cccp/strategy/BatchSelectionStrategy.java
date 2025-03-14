package cccp.strategy;

import java.util.Date;
import java.util.List;

import cccp.model.Batch;

public interface BatchSelectionStrategy {
	List<Batch> selectBatch(List<Batch> batches, int quantity, Date currentDate);
}
