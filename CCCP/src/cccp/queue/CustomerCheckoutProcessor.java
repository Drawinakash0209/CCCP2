package cccp.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomerCheckoutProcessor {
    private static final int THREAD_POOL_SIZE = 4;
    private static final int QUEUE_CAPACITY = 100;
    private static final BlockingQueue<CheckoutRequest> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private static final ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(new CheckoutWorker());
        }
    }

    public static void addCheckoutRequest(CheckoutRequest request) {
        if (!queue.offer(request)) {
            if (request instanceof OnlineCheckoutRequest) {
                ((OnlineCheckoutRequest) request).getResultFuture().complete(
                    new CheckoutResult("failed", "Queue is full"));
            }
        }
    }

    public static void shutdown() {
        executorService.shutdown();
    }

    private static class CheckoutWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    CheckoutRequest request = queue.take();
                    processCheckoutRequest(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error processing checkout: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        private void processCheckoutRequest(CheckoutRequest request) {
            try {
                request.execute();
                System.out.println("✅ [Thread: " + Thread.currentThread().getName() + "] Processed checkout request: " + request.getResultKey());
            } catch (Exception e) {
                if (request instanceof OnlineCheckoutRequest) {
                    ((OnlineCheckoutRequest) request).getResultFuture().complete(
                        new CheckoutResult("failed", "Error processing checkout: " + e.getMessage()));
                }
                System.err.println("❌ [Thread: " + Thread.currentThread().getName() + "] Failed to process checkout request: " + request.getResultKey());
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