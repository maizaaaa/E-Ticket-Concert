// generate simple report on the bottom
import java.util.List;

public class SalesAnalyzer {

    public static void generateReport(TicketManager manager, ETicketSystemGUI gui) {
        gui.logMessage("\n==================================================");
        gui.logMessage("                 SALES CONCLUDED                  ");
        gui.logMessage("==================================================");

        List<BookingRequest> successfulList = manager.getSuccessfulBookings();

        long totalSuccessfulUsers = successfulList.parallelStream().count();

        int totalTicketsSold = successfulList.parallelStream()
                .mapToInt(BookingRequest::getPax)
                .sum();

        gui.logMessage("Remaining Tickets: " + manager.getRemainingTickets());
        gui.logMessage("Total Successful Buyers: " + totalSuccessfulUsers);
        gui.logMessage("Total Tickets Distributed: " + totalTicketsSold);

        gui.logMessage("\n--- Concurrency Validation Test ---");
        if (totalTicketsSold + manager.getRemainingTickets() == manager.getTotalTickets()) {
            gui.logMessage("[PASS] Data integrity maintained. No tickets oversold.");
        } else {
            gui.logMessage("[FAIL] Race condition detected! Ticket count mismatch.");
        }
    }
}
