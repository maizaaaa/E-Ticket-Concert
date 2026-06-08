import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ETicketSystemGUI extends JFrame {

    private JLabel ticketLabel;
    private JTextArea systemLogArea;
    private JTextArea consumerLogArea;
    private JTextField nameInput;
    private JComboBox<Integer> paxInput;
    private JButton buyButton;
    private JButton simulateButton;

    private TicketManager manager;

    public ETicketSystemGUI() {

        manager = new TicketManager(50);

        // ==========================================
        // FRAME SETTINGS & FAVICON
        // ==========================================
        setTitle("E-Ticket Purchasing System");
        setSize(1000, 650); // Slightly wider to accommodate two panels comfortably
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        java.net.URL iconURL = getClass().getResource("/assets/favicon.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            this.setIconImage(icon.getImage());
        } else {
            System.out.println("Warning: favicon.png not found in /assets/");
        }

        // Main background
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(15, 15));

        // ==========================================
        // HEADER PANEL
        // ==========================================
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 41, 59));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        headerPanel.setLayout(new GridLayout(3, 1, 5, 5));

        JLabel titleLabel = new JLabel("Concert E-Ticket Purchasing System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel concertLabel = new JLabel("SEVENTEEN World Tour 2026", SwingConstants.CENTER);
        concertLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        concertLabel.setForeground(new Color(220, 220, 220));

        ticketLabel = new JLabel("Available Tickets: 50", SwingConstants.CENTER);
        ticketLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        ticketLabel.setForeground(new Color(0, 255, 140));

        headerPanel.add(titleLabel);
        headerPanel.add(concertLabel);
        headerPanel.add(ticketLabel);

        add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // CENTER PANEL (SIDE-BY-SIDE LOGS)
        // ==========================================
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Producer Log Area
        systemLogArea = new JTextArea();
        systemLogArea.setEditable(false);
        systemLogArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        systemLogArea.setBackground(new Color(250, 250, 250)); // Slightly off-white to distinguish
        systemLogArea.setForeground(new Color(0, 50, 150)); // Dark blue text for system
        systemLogArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane sysScroll = new JScrollPane(systemLogArea);
        sysScroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(180, 180, 180), 1),
                "Engine and Summary Log",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)
        ));

        // Consumer Log Area
        consumerLogArea = new JTextArea();
        consumerLogArea.setEditable(false);
        consumerLogArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        consumerLogArea.setBackground(Color.WHITE);
        consumerLogArea.setForeground(Color.BLACK);
        consumerLogArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane consScroll = new JScrollPane(consumerLogArea);
        consScroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(180, 180, 180), 1),
                "Consumer Logs",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)
        ));

        centerPanel.add(sysScroll);
        centerPanel.add(consScroll);
        add(centerPanel, BorderLayout.CENTER);

        // ==========================================
        // BOTTOM PANEL (FORM & BUTTONS)
        // ==========================================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel nameLabel = new JLabel("Buyer Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameInput = new JTextField();
        nameInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel paxLabel = new JLabel("Number of Tickets:");
        paxLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        Integer[] paxOptions = {1, 2, 3, 4};
        paxInput = new JComboBox<>(paxOptions);
        paxInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(nameLabel);
        formPanel.add(nameInput);
        formPanel.add(paxLabel);
        formPanel.add(paxInput);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));

        buyButton = new JButton("Buy Ticket");
        styleButton(buyButton, new Color(34, 197, 94));

        simulateButton = new JButton("Simulate 100 Users");
        styleButton(simulateButton, new Color(239, 68, 68));

        buttonPanel.add(buyButton);
        buttonPanel.add(simulateButton);
        bottomPanel.add(formPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // BUTTON ACTIONS
        buyButton.addActionListener(e -> {
            String buyerName = nameInput.getText().trim();
            if (buyerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Buyer Name!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int requestedPax = (int) paxInput.getSelectedItem();
            Thread buyerThread = new Thread(new Buyer(manager, buyerName, requestedPax, this));
            buyerThread.start();
            nameInput.setText("");
        });

        simulateButton.addActionListener(e -> {
            simulateButton.setEnabled(false);
            new Thread(() -> SimulationEngine.startSimulation(manager, this, simulateButton)).start();
        });
    }

    private void styleButton(JButton button, Color color) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 45));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void updateTicketLabel(int tickets) {
        SwingUtilities.invokeLater(() -> {
            if (tickets > 0) {
                ticketLabel.setText("Available Tickets: " + tickets);
            } else {
                ticketLabel.setForeground(Color.RED);
                ticketLabel.setText("SOLD OUT");
            }
        });
    }

    public void logSystemMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            systemLogArea.append(message + "\n");
            systemLogArea.setCaretPosition(systemLogArea.getDocument().getLength());
        });
    }

    public void logConsumerMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            consumerLogArea.append(message + "\n");
            consumerLogArea.setCaretPosition(consumerLogArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ETicketSystemGUI().setVisible(true));
    }
}