
//joining threads abd creating the heavy load

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class SimulationEngine {

    public static void startSimulation(TicketManager manager, ETicketSystemGUI gui, JButton simulateButton) {
        gui.logMessage("\n===== STARTING 100 USER SIMULATION =====\n");

        Thread[] threads = new Thread[100];

        // Create heavy concurrent load (Spooling)
        for (int i = 0; i < 100; i++) {
            int randomPax = (int) (Math.random() * 3) + 1;
            Buyer buyer = new Buyer(manager, "User-" + (i + 1), randomPax, gui);
            threads[i] = new Thread(buyer);

            // Thread priority influencing
            threads[i].setPriority(Thread.NORM_PRIORITY);
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SalesAnalyzer.generateReport(manager, gui);

        SwingUtilities.invokeLater(() -> simulateButton.setEnabled(true));
    }
}
