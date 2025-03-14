package test.product;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cccp.model.Batch;
import cccp.model.Product;

class ProductTest {

	private Product product;
    private List<Batch> batches;
    
    @BeforeEach
    void setUp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // Create a sample product 
            product = new Product.Builder()
                    .setId("P123")
                    .setName("Sample Product")
                    .setPrice(50.0)
                    .setCategoryId(1)
                    .setReorderLevel(10)
                    .setBatches(Arrays.asList(
                            new Batch("B1", 5, dateFormat.parse("2025-01-24"), dateFormat.parse("2025-01-24")),
                            new Batch("B2", 5, dateFormat.parse("2025-01-20"), dateFormat.parse("2025-01-29"))
                    ))
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Failed to parse date in setup method");
        }
    }
    
    
    @Test
    void testProductCreation() {
        assertEquals("P123", product.getId());
        assertEquals("Sample Product", product.getName());
        assertEquals(50.0, product.getPrice());
        assertEquals(1, product.getCategoryId());
        assertEquals(10, product.getQuantity()); 
        assertEquals(10, product.getReorderLevel());
    }
    
    @Test
    void testAddBatch() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
  
            Date purchaseDate = dateFormat.parse("2025-01-24");
            Date expiryDate = dateFormat.parse("2025-01-24");
            // Create a new Batch object
            Batch newBatch = new Batch("B3", 10, purchaseDate, expiryDate);
            product.addBatch(newBatch);
            
            // Assertions
            assertEquals(3, product.getBatches().size());
            assertEquals(20, product.getQuantity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Test
    void testSetReorderLevel() {
        Product updatedProduct = new Product.Builder()
                .setId("P123")
                .setName("Sample Product")
                .setPrice(50.0)
                .setCategoryId(1)
                .setReorderLevel(5)
                .build();
        assertEquals(5, updatedProduct.getReorderLevel());
    }
    
    
   
    
    

}
