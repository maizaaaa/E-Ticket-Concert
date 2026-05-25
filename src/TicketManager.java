import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketManager {

    private final int TOTAL_TICKETS;
    private int availableTickets;

    // Stores data for the Parallel Streams processing
    private final List<BookingRequest> successfulBookings = new ArrayList<>();

    // synchronization
    private final Lock lock = new ReentrantLock(true);

    public TicketManager(int tickets) {
        this.TOTAL_TICKETS = tickets;
        this.availableTickets = tickets;
    }

    /**
     * The Critical Section
     */
    public boolean bookTicket(String userName, int requestedPax, ETicketSystemGUI gui) {
        gui.logMessage("[SPOOLING] " + userName + " is trying to buy " + requestedPax + " ticket(s)...");

        lock.lock();

        try {
            Thread.sleep(20);

            if (availableTickets >= requestedPax) {
                availableTickets -= requestedPax;

                //successfulBookings.add(new REFERENCE.BookingRequest(userName, requestedPax));

                gui.logMessage("  -> [SUCCESS] " + userName + " bought " + requestedPax + " ticket(s). Remaining: " + availableTickets);
                gui.updateTicketLabel(availableTickets);
                return true;
            } else {
                gui.logMessage("  -> [FAILED] " + userName + " requested " + requestedPax + ", but only " + availableTickets + " left.");
                return false;
            }

        } catch (InterruptedException e) {
            gui.logMessage(userName + " interrupted.");
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Getters used by the SalesAnalyzer
    public List<BookingRequest> getSuccessfulBookings() {
        return successfulBookings;
    }

    public int getRemainingTickets() {
        return availableTickets;
    }

    public int getTotalTickets() {
        return TOTAL_TICKETS;
    }
}