package cccp.queue;

public interface CheckoutRequest {
    void execute() throws Exception;
    String getResultKey();
}