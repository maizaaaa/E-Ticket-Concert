import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ETicketSystemGUI extends JFrame {

    private JLabel ticketLabel;
    private JTextArea logArea;
    private JTextField nameInput;
    private JComboBox<Integer> paxInput;

    private JButton buyButton;
    private JButton simulateButton;

    private TicketManager manager;

    public ETicketSystemGUI() {

        // Initialize ticket manager
        manager = new TicketManager(50);

        // ==========================================
        // FRAME SETTINGS & FAVICON
        // ==========================================
        setTitle("E-Ticket Purchasing System");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Memuat dan memasang favicon.png dari folder src/assets
        java.net.URL iconURL = getClass().getResource("/assets/favicon.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            this.setIconImage(icon.getImage());
        } else {
            System.out.println("Peringatan: favicon.png tidak ditemukan di /assets/");
        }
        // ==========================================

        // Main background
        getContentPane().setBackground(
                new Color(245, 247, 250)
        );

        setLayout(new BorderLayout(15, 15));

        // header panel
        JPanel headerPanel = new JPanel();

        headerPanel.setBackground(
                new Color(30, 41, 59)
        );

        headerPanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        headerPanel.setLayout(
                new GridLayout(3, 1, 5, 5)
        );

        JLabel titleLabel = new JLabel(
                "Concert E-Ticket Purchasing System",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        28)
        );

        titleLabel.setForeground(Color.WHITE);

        JLabel concertLabel = new JLabel(
                "SEVENTEEN World Tour 2026",
                SwingConstants.CENTER
        );

        concertLabel.setFont(
                new Font("Segoe UI",
                        Font.PLAIN,
                        18)
        );

        concertLabel.setForeground(
                new Color(220, 220, 220)
        );

        ticketLabel = new JLabel(
                "Available Tickets: 50",
                SwingConstants.CENTER
        );

        ticketLabel.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        20)
        );

        ticketLabel.setForeground(
                new Color(0, 255, 140)
        );

        headerPanel.add(titleLabel);
        headerPanel.add(concertLabel);
        headerPanel.add(ticketLabel);

        add(headerPanel, BorderLayout.NORTH);

        // center panel
        JPanel centerPanel = new JPanel(
                new BorderLayout()
        );

        centerPanel.setBackground(
                new Color(245, 247, 250)
        );

        centerPanel.setBorder(
                new EmptyBorder(10, 20, 10, 20)
        );

        logArea = new JTextArea();

        logArea.setEditable(false);

        logArea.setFont(
                new Font("Consolas",
                        Font.PLAIN,
                        14)
        );

        logArea.setBackground(Color.WHITE);

        logArea.setForeground(Color.BLACK);

        logArea.setMargin(
                new Insets(10, 10, 10, 10)
        );

        JScrollPane scrollPane =
                new JScrollPane(logArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(
                                new Color(180, 180, 180),
                                1
                        ),
                        "Real-Time Booking Logs",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                14
                        )
                )
        );

        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(
                new BorderLayout()
        );

        bottomPanel.setBackground(
                new Color(245, 247, 250)
        );

        bottomPanel.setBorder(
                new EmptyBorder(10, 20, 20, 20)
        );

        // form user panel
        JPanel formPanel = new JPanel(
                new GridLayout(2, 2, 15, 15)
        );

        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                new Color(220, 220, 220)
                        ),
                        new EmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        JLabel nameLabel =
                new JLabel("Buyer Name:");
        nameLabel.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        14)
        );

        nameInput = new JTextField();
        nameInput.setFont(
                new Font("Segoe UI",
                        Font.PLAIN,
                        14)
        );

        JLabel paxLabel =
                new JLabel("Number of Tickets:");
        paxLabel.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        14)
        );

        Integer[] paxOptions = {1, 2, 3, 4};
        paxInput = new JComboBox<>(paxOptions);
        paxInput.setFont(
                new Font("Segoe UI",
                        Font.PLAIN,
                        14)
        );

        formPanel.add(nameLabel);
        formPanel.add(nameInput);

        formPanel.add(paxLabel);
        formPanel.add(paxInput);

        // button panel
        JPanel buttonPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        20,
                        15
                )
        );

        buttonPanel.setBackground(
                new Color(245, 247, 250)
        );

        buyButton = new JButton(
                "Buy Ticket"
        );

        styleButton(
                buyButton,
                new Color(34, 197, 94)
        );

        simulateButton = new JButton(
                "Simulate 100 Users"
        );

        styleButton(
                simulateButton,
                new Color(239, 68, 68)
        );

        buttonPanel.add(buyButton);
        buttonPanel.add(simulateButton);
        bottomPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(bottomPanel, BorderLayout.SOUTH);

        //button action for manual purchase
        buyButton.addActionListener(e -> {
            String buyerName =
                    nameInput.getText().trim();
            if (buyerName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter Buyer Name!",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            int requestedPax =
                    (int) paxInput.getSelectedItem();
            Thread buyerThread = new Thread(
                    new Buyer(
                            manager,
                            buyerName,
                            requestedPax,
                            this
                    )
            );
            buyerThread.start();
            nameInput.setText("");
        });

        //simulate 100 users
        simulateButton.addActionListener(e -> {
            simulateButton.setEnabled(false);
            logMessage(
                    "\n===== STARTING 100 USER SIMULATION =====\n"
            );
            for (int i = 1; i <= 100; i++) {
                String userName = "User-" + i;

                // Setiap user membeli 1 tiket
                Thread userThread = new Thread(new Buyer(manager, userName, 1, this));
                userThread.start(); // Menjalankan thread secara asynchronous/bersamaan
            }
            //** THREAD SLEEP AND JOIN **//
        });
    }

    // button style
    private void styleButton(
            JButton button,
            Color color
    ) {
        button.setFocusPainted(false);
        button.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        14)
        );

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(
                new Dimension(200, 45)
        );

        button.setBorder(
                new EmptyBorder(10, 20, 10, 20)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    // update ticket label
    public void updateTicketLabel(int tickets) {
        SwingUtilities.invokeLater(() -> {
            if (tickets > 0) {
                ticketLabel.setText(
                        "Available Tickets: " + tickets
                );
            }
            else {
                ticketLabel.setForeground(Color.RED);
                ticketLabel.setText("SOLD OUT");
            }
        });
    }

    // log message on center
    public void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(
                    logArea.getDocument().getLength()
            );
        });
    }

    // main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ETicketSystemGUI()
                    .setVisible(true);
        });
    }
}