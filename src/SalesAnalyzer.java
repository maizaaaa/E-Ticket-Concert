import java.util.List;
import java.util.stream.Collectors;

public class SalesAnalyzer {

    public static void generateReport(TicketManager manager, ETicketSystemGUI gui) {
        gui.logSystemMessage("\n==================================================");
        gui.logSystemMessage("                 SALES CONCLUDED                  ");
        gui.logSystemMessage("==================================================");

        List<BookingRequest> successfulList = manager.getSuccessfulBookings();

        long totalSuccessfulUsers = successfulList.parallelStream().count();

        int totalTicketsSold = successfulList.parallelStream()
                .mapToInt(BookingRequest::getPax)
                .sum();

        gui.logSystemMessage("Remaining Tickets: " + manager.getRemainingTickets());
        gui.logSystemMessage("Total Successful Buyers: " + totalSuccessfulUsers);
        gui.logSystemMessage("Total Tickets Distributed: " + totalTicketsSold);

        gui.logSystemMessage("\n==================================================");
        gui.logSystemMessage("                  WINNER (" + totalSuccessfulUsers + " users)              ");
        gui.logSystemMessage("==================================================");

        String successNames = successfulList.stream()
                .map(BookingRequest::getUserId)
                .collect(Collectors.joining("\n"));
        gui.logSystemMessage(successNames);

        gui.logSystemMessage("\n--- Concurrency Validation Test ---");
        if (totalTicketsSold + manager.getRemainingTickets() == manager.getTotalTickets()) {
            gui.logSystemMessage("[PASS] Data integrity maintained. No tickets oversold.");
        } else {
            gui.logSystemMessage("[FAIL] Race condition detected! Ticket count mismatch.");
        }
    }
}