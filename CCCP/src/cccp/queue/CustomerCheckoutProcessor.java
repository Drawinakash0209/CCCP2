package cccp.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomerCheckoutProcessor {
    private static final int THREAD_POOL_SIZE = 4;
    private static final BlockingQueue<CheckoutRequest> queue = new LinkedBlockingQueue<>();
    private static final ConcurrentHashMap<String, CheckoutResult> checkoutResults = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            Thread worker = new Thread(new CheckoutWorker(), "CheckoutWorker-" + i);
            worker.setDaemon(true);
            worker.start();
        }
    }

    public static void addCheckoutRequest(CheckoutRequest request) {
        checkoutResults.put(request.getResultKey(), new CheckoutResult("pending", null));
        queue.offer(request);
    }

    public static CheckoutResult getCheckoutResult(String resultKey) {
        return checkoutResults.get(resultKey);
    }

    public static void clearCheckoutResult(String resultKey) {
        checkoutResults.remove(resultKey);
    }

    private static class CheckoutWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    CheckoutRequest request = queue.take();
                    processCheckoutRequest(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void processCheckoutRequest(CheckoutRequest request) {
            String resultKey = request.getResultKey();
            try {
                request.execute();
                System.out.println("✅ [Thread: " + Thread.currentThread().getName() + "] Processed checkout request: " + resultKey);
            } catch (Exception e) {
                checkoutResults.put(resultKey, new CheckoutResult("failed", "Error processing checkout: " + e.getMessage()));
                System.err.println("❌ [Thread: " + Thread.currentThread().getName() + "] Failed to process checkout request: " + resultKey);
                e.printStackTrace();
            }
        }
    }

    public static class CheckoutResult {
        public String status;
        public Object message; // Can be Bill or error message
        public CheckoutResult(String status, Object message) {
            this.status = status;
            this.message = message;
        }
    }
}