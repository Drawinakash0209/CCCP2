package test.sale;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import cccp.model.Sale;

class SaleTest {

	@Test
    void testSaleBuilderWithAllFields() {
        // Arrange
        Date salesDate = new Date();
        String productCode = "P001";
        int quantitySold = 5;
        double totalRevenue = 100.0;
        String saleType = "INSTORE";

        // Act
        Sale sale = new Sale.SaleBuilder()
                .setSaleId(1)
                .setProductCode(productCode)
                .setQuantitySold(quantitySold)
                .setTotalRevenue(totalRevenue)
                .setSalesDate(salesDate)
                .setSaleType(saleType)
                .build();

        // Assert
        assertNotNull(sale);
        assertEquals(1, sale.getSalesId());
        assertEquals(productCode, sale.getProductCode());
        assertEquals(quantitySold, sale.getQuantitySold());
        assertEquals(totalRevenue, sale.getTotalRevenue());
        assertEquals(salesDate, sale.getSalesDate());
        assertEquals(saleType, sale.getSaleType());
    }

    @Test
    void testSaleBuilderWithNegativeQuantity() {
        // Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Sale.SaleBuilder()
                    .setSaleId(1)
                    .setProductCode("P001")
                    .setQuantitySold(-5) // Invalid quantity
                    .setTotalRevenue(100.0)
                    .setSalesDate(new Date())
                    .build();
        });

        assertEquals("quantitySold must be positive", exception.getMessage());
    }

    @Test
    void testSaleCreationMethod() {
        // Arrange
        Date salesDate = new Date();
        String productCode = "P002";
        int quantitySold = 3;
        double totalRevenue = 150.0;
        String saleType = "ONLINE";

        // Act
        Sale sale = Sale.saleCreation(2, productCode, quantitySold, totalRevenue, salesDate, saleType);

        // Assert
        assertNotNull(sale);
        assertEquals(2, sale.getSalesId());
        assertEquals(productCode, sale.getProductCode());
        assertEquals(quantitySold, sale.getQuantitySold());
        assertEquals(totalRevenue, sale.getTotalRevenue());
        assertEquals(salesDate, sale.getSalesDate());
        assertEquals(saleType, sale.getSaleType());
    }
}
