package cccp.model;

public class DeliveryDetails {
    private int id;
    private int billId;
    private int customerId;
    private String name;
    private String phoneNumber;
    private String deliveryAddress;

    // Constructor for creating new delivery details
    public DeliveryDetails(int billId, int customerId, String name, String phoneNumber, String deliveryAddress) {
        this.billId = billId;
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
    }

    // Constructor for retrieving from database
    public DeliveryDetails(int id, int billId, int customerId, String name, String phoneNumber, String deliveryAddress) {
        this.id = id;
        this.billId = billId;
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}