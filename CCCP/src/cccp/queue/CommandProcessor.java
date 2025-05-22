package cccp.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import cccp.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CommandProcessor {
    private static final int THREAD_POOL_SIZE = 4; // Match CustomerCheckoutProcessor
    private static final int QUEUE_CAPACITY = 100; // Bounded queue
    private static final BlockingQueue<CommandRequest> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private static final ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(new CommandWorker());
        }
    }

    public static void addCommand(Command command, HttpServletRequest request, HttpServletResponse response) {
        CommandRequest cmdRequest = new CommandRequest(command, request, response);
        if (!queue.offer(cmdRequest)) {
            command.getResultFuture().complete(new CommandResult("failed", "Queue is full"));
        }
    }

    public static void shutdown() {
        executorService.shutdown();
    }

    private static class CommandWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    CommandRequest cmdRequest = queue.take();
                    processCommand(cmdRequest);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        private void processCommand(CommandRequest cmdRequest) {
            Command command = cmdRequest.getCommand();
            try {
                command.execute();
                System.out.println("✅ [Thread: " + Thread.currentThread().getName() + "] Processed command: " + command.getResultKey());
            } catch (Exception e) {
                command.getResultFuture().complete(new CommandResult("failed", "Error processing command: " + e.getMessage()));
                System.err.println("❌ [Thread: " + Thread.currentThread().getName() + "] Failed to process command: " + command.getResultKey());
                e.printStackTrace();
            }
        }
    }

    public static class CommandRequest {
        private final Command command;
        private final HttpServletRequest request;
        private final HttpServletResponse response;

        public CommandRequest(Command command, HttpServletRequest request, HttpServletResponse response) {
            this.command = command;
            this.request = request;
            this.response = response;
        }

        public Command getCommand() {
            return command;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public HttpServletResponse getResponse() {
            return response;
        }
    }

    public static class CommandResult {
        public String status;
        public String message;

        public CommandResult(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}