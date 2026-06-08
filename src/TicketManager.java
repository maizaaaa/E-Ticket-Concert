import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CopyOnWriteArrayList;

public class TicketManager {

    private final int TOTAL_TICKETS;
    private final List<BookingRequest> successfulBookings = new ArrayList<>(); //parallel stream
    private CopyOnWriteArrayList<String> ticketPool = new CopyOnWriteArrayList<>(); //parent handle all child
    private final Lock lock = new ReentrantLock(true);  // synchronization
    private final Condition ticketsAvailable = lock.newCondition();

    private boolean isSoldOut = false;

    public TicketManager(int tickets) {
        this.TOTAL_TICKETS = tickets;
    }

    public void produceTickets(int amount, ETicketSystemGUI gui){
        lock.lock();
        try{
            for (int i = 0; i<amount; i++){
                ticketPool.add("TKT-" + System.nanoTime());
            }
            gui.logSystemMessage("\n[PRODUCER] Added " + amount + " tickets. Total in pool: " + ticketPool.size() + "\n");

            gui.updateTicketLabel(ticketPool.size());

            ticketsAvailable.signalAll();
        } finally{
            lock.unlock();
        }
    }

    public void setSimulationSoldOut() {
        lock.lock();
        try {
            isSoldOut = true;
            ticketsAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean bookTicket(String userName, int requestedPax, ETicketSystemGUI gui) {
        gui.logConsumerMessage("[SPOOLING] " + userName + " is trying to buy " + requestedPax + " ticket(s)...");

        lock.lock();
        try {
            while (ticketPool.size() < requestedPax) {
                if (isSoldOut) {
                    int leftoverTickets = ticketPool.size();

                    if (leftoverTickets > 0) {
                        gui.logConsumerMessage("  -> [PARTIAL] " + userName + " wanted " + requestedPax + " but only " + leftoverTickets + " left. Taking them!");

                        requestedPax = leftoverTickets;

                        break;
                    } else {
                        gui.logConsumerMessage("  -> [FAILED] " + userName + " requested " + requestedPax + ", but simulation is completely SOLD OUT.");
                        return false;
                    }
                }

                gui.logConsumerMessage("  -> [WAITING] " + userName + " needs " + requestedPax + " but only " + ticketPool.size() + " are available.");
                ticketsAvailable.await();
            }

            // BUY PHASE
            Thread.sleep(20);

            for (int i = 0; i < requestedPax; i++) {
                ticketPool.remove(ticketPool.size() - 1);
            }

            successfulBookings.add(new BookingRequest(userName, requestedPax));

            gui.logConsumerMessage("  -> [SUCCESS] " + userName + " bought " + requestedPax + " ticket(s). Remaining: " + ticketPool.size());
            gui.updateTicketLabel(ticketPool.size());

            return true;

        } catch (InterruptedException e) {
            gui.logConsumerMessage(userName + " interrupted.");
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    public List<BookingRequest> getSuccessfulBookings() {
        return successfulBookings;
    }

    public int getRemainingTickets() {
        return ticketPool.size();
    }

    public int getTotalTickets() {
        return TOTAL_TICKETS;
    }
}