package cccp.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import cccp.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CommandProcessor {
    private static final int THREAD_POOL_SIZE = 5;
    private static final BlockingQueue<CommandRequest> queue = new LinkedBlockingQueue<>();
    private static final ConcurrentHashMap<String, CommandResult> commandResults = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            Thread worker = new Thread(new CommandWorker(), "CommandWorker-" + i);
            worker.setDaemon(true);
            worker.start();
        }
    }

    public static void addCommand(Command command, HttpServletRequest request, HttpServletResponse response) {
        CommandRequest cmdRequest = new CommandRequest(command, request, response);
        queue.offer(cmdRequest);
    }

    public static CommandResult getCommandResult(String resultKey) {
        return commandResults.get(resultKey);
    }

    public static void clearCommandResult(String resultKey) {
        commandResults.remove(resultKey);
    }

    private static class CommandWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    CommandRequest cmdRequest = queue.take();
                    processCommand(cmdRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void processCommand(CommandRequest cmdRequest) {
            Command command = cmdRequest.getCommand();
            String resultKey = command.getResultKey();
            CommandResult result = new CommandResult();

            try {
                command.execute();
                result.status = "success";
                result.message = "Command executed successfully";
            } catch (Exception e) {
                result.status = "failed";
                result.message = "Error executing command: " + e.getMessage();
                e.printStackTrace();
            }

            commandResults.put(resultKey, result);
            System.out.println("✅ [Thread: " + Thread.currentThread().getName() + "] Processed command: " + resultKey);
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
        public String status = "pending";
        public String message = null;
    }
}