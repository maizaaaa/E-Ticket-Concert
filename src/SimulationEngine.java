import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class SimulationEngine {

    public static void startSimulation(TicketManager manager, ETicketSystemGUI gui, JButton simulateButton) {
        gui.logSystemMessage("\n===== STARTING 100 USER SIMULATION =====\n");

        new Thread(() -> {

            // PRODUCER Thread
            Thread producerThread = new Thread(() -> {
                for (int i = 0; i < 2; i++) {
                    manager.produceTickets(25, gui);
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                gui.logSystemMessage("[PRODUCER] Finished generating all 100 tickets. Announcing SOLD OUT");
                manager.setSimulationSoldOut();
            });
            producerThread.start();

            // 100 CONSUMER Threads (Buyers)
            Thread[] threads = new Thread[100];
            for (int i = 0; i < 100; i++) {
                int randomPax = (int) (Math.random() * 4) + 1;
                Buyer buyer = new Buyer(manager, "User-" + (i + 1), randomPax, gui);
                threads[i] = new Thread(buyer);

                threads[i].setPriority(Thread.NORM_PRIORITY);
                threads[i].start();
            }

            // to finish the thread
            try {
                producerThread.join();
                for (Thread t : threads) {
                    t.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // GUI Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                SalesAnalyzer.generateReport(manager, gui);
                simulateButton.setEnabled(true);
                gui.logSystemMessage("\n===== SIMULATION COMPLETE =====");
            });

        }).start();
    }
}