package cccp.queue;

import cccp.model.Bill;
import cccp.model.DeliveryDetails;
import cccp.model.Product;
import cccp.model.User;
import cccp.model.dao.DeliveryDetailsDAOInterface;
import cccp.service.BillingServiceInterface;
import cccp.service.OnlineOrderService;

import java.util.Map;

public class OnlineCheckoutRequest implements CheckoutRequest {
    private final BillingServiceInterface billingService;
    private final DeliveryDetailsDAOInterface deliveryDetailsDAO;
    private final OnlineOrderService onlineOrderService;
    private final Map<Product, Integer> itemsToPurchase;
    private final User user;
    private final String name;
    private final String phoneNumber;
    private final String deliveryAddress;
    private final String resultKey;
    private CustomerCheckoutProcessor.CheckoutResult result;

    public OnlineCheckoutRequest(BillingServiceInterface billingService, DeliveryDetailsDAOInterface deliveryDetailsDAO,
                                 OnlineOrderService onlineOrderService, Map<Product, Integer> itemsToPurchase, User user,
                                 String name, String phoneNumber, String deliveryAddress) {
        this.billingService = billingService;
        this.deliveryDetailsDAO = deliveryDetailsDAO;
        this.onlineOrderService = onlineOrderService;
        this.itemsToPurchase = itemsToPurchase;
        this.user = user;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.resultKey = java.util.UUID.randomUUID().toString();
        this.result = new CustomerCheckoutProcessor.CheckoutResult("pending", null);
    }

    @Override
    public void execute() throws Exception {
        try {
            Bill generatedBill = billingService.generateOnlineBill(user.getId(), itemsToPurchase);
            if (generatedBill != null) {
                DeliveryDetails deliveryDetails = new DeliveryDetails(
                    generatedBill.getBillId(), user.getId(), name, phoneNumber, deliveryAddress
                );
                int saveResult = deliveryDetailsDAO.saveDeliveryDetails(deliveryDetails);
                if (saveResult > 0) {
                    System.out.println("✅ [Thread: " + Thread.currentThread().getName() + "] Delivery details saved for bill ID: " + generatedBill.getBillId());
                    // Update ConcurrentHashMap first
                    CustomerCheckoutProcessor.getCheckoutResult(getResultKey()).status = "success";
                    CustomerCheckoutProcessor.getCheckoutResult(getResultKey()).message = generatedBill;
                    this.result = new CustomerCheckoutProcessor.CheckoutResult("success", generatedBill);
                } else {
                    throw new Exception("Failed to save delivery details for bill ID: " + generatedBill.getBillId());
                }
            } else {
                throw new Exception("Failed to generate bill for user ID: " + user.getId());
            }
        } catch (Exception e) {
            CustomerCheckoutProcessor.getCheckoutResult(getResultKey()).status = "failed";
            CustomerCheckoutProcessor.getCheckoutResult(getResultKey()).message = e.getMessage();
            this.result = new CustomerCheckoutProcessor.CheckoutResult("failed", e.getMessage());
            throw e;
        }
    }

    @Override
    public String getResultKey() {
        return resultKey;
    }

    @Override
    public CustomerCheckoutProcessor.CheckoutResult getResult() {
        return result;
    }
}