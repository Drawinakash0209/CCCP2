package test.services;

import cccp.model.Batch;
import cccp.model.Product;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private BatchDAOInterface batchDAO;

    @Mock
    private Product product;

    @Mock
    private Batch batch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productDAO, batchDAO);
    }

    @Test
    void testAddBatchToProduct_Success() {
        // Arrange
        String productId = "P001";
        when(productDAO.getProductById(productId)).thenReturn(product);
        when(batchDAO.getTotalQuantityForProduct(productId)).thenReturn(100);

        // Act
        productService.addBatchToProduct(productId, batch);

        // Assert
        verify(batchDAO).addBatch(batch, productId);
        verify(productDAO).getProductById(productId);
        verify(batchDAO).getTotalQuantityForProduct(productId);
        verify(productDAO).updateProductQuantity(productId, 100);
    }

    @Test
    void testRecalculateProductQuantity_Success() {
        // Arrange
        String productId = "P001";
        when(productDAO.getProductById(productId)).thenReturn(product);
        when(batchDAO.getTotalQuantityForProduct(productId)).thenReturn(100);

        // Act
        productService.recalculateProductQuantity(productId);

        // Assert
        verify(productDAO).getProductById(productId);
        verify(batchDAO).getTotalQuantityForProduct(productId);
        verify(productDAO).updateProductQuantity(productId, 100);
    }

    @Test
    void testRecalculateProductQuantity_NullProduct() {
        // Arrange
        String productId = "P001";
        when(productDAO.getProductById(productId)).thenReturn(null);

        // Act
        productService.recalculateProductQuantity(productId);

        // Assert
        verify(productDAO).getProductById(productId);
        verifyNoInteractions(batchDAO, product);
        verifyNoMoreInteractions(productDAO);
    }

    @Test
    void testAddProduct_Success() {
        // Arrange
        // Product is already mocked

        // Act
        productService.addProduct(product);

        // Assert
        verify(productDAO).addItem(product);
        verifyNoInteractions(batchDAO);
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        // Product is already mocked

        // Act
        productService.updateProduct(product);

        // Assert
        verify(productDAO).updateItem(product);
        verifyNoInteractions(batchDAO);
    }

    @Test
    void testDeleteProduct_Success() {
        // Arrange
        String productId = "P001";

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productDAO).removeItem(productId);
        verifyNoInteractions(batchDAO);
    }

    @Test
    void testGetAllProducts_Success() {
        // Arrange
        List<Product> expectedProducts = List.of(product);
        when(productDAO.getAllProducts()).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(expectedProducts, result);
        verify(productDAO).getAllProducts();
        verifyNoInteractions(batchDAO);
    }

    @Test
    void testGetAllProducts_EmptyList() {
        // Arrange
        List<Product> expectedProducts = List.of();
        when(productDAO.getAllProducts()).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertTrue(result.isEmpty());
        verify(productDAO).getAllProducts();
        verifyNoInteractions(batchDAO);
    }

    @Test
    void testOnShelfRestocked_Success() {
        // Arrange
        String productId = "P001";
        when(productDAO.getProductById(productId)).thenReturn(product);
        when(batchDAO.getTotalQuantityForProduct(productId)).thenReturn(100);

        // Act
        productService.onShelfRestocked(productId);

        // Assert
        verify(productDAO).getProductById(productId);
        verify(batchDAO).getTotalQuantityForProduct(productId);
        verify(productDAO).updateProductQuantity(productId, 100);
    }


}