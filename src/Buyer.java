class Buyer implements Runnable {
    private TicketManager manager;
    private String userName;
    private int requestedPax;
    private ETicketSystemGUI gui;

    public Buyer(TicketManager manager, String userName, int requestedPax, ETicketSystemGUI gui) {
        this.manager = manager;
        this.userName = userName;
        this.requestedPax = requestedPax;
        this.gui = gui;
    }

    @Override
    public void run() {
        try {
            // Thread influencing to simulate network delay
            Thread.sleep((long) (Math.random() * 200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        manager.bookTicket(userName, requestedPax, gui);
    }
}
