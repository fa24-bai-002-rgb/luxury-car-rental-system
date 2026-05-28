package ui;

import database.Database;
import models.Booking;
import models.Car;
import models.Customer;
import utils.ColorScheme;
import utils.GradientPanel;
import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MainDashboardFrame extends JFrame {
    private Customer customer;
    private Database database;
    private JPanel contentPanel;

    public MainDashboardFrame(Customer customer) {
        this.customer = customer;
        this.database = Database.getInstance();

        setTitle("Luxury Car Rental System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorScheme.BACKGROUND_DARK);

        // Header Panel
        JPanel headerPanel = new GradientPanel(ColorScheme.PRIMARY_DARK, 
                ColorScheme.PRIMARY_LIGHT, GradientPanel.HORIZONTAL);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("🏎️ LUXURY RENTALS - Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ColorScheme.ACCENT_GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));

        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        RoundedButton logoutBtn = new RoundedButton("LOGOUT", ColorScheme.BUTTON_DANGER,
                new Color(255, 87, 34), new Color(229, 57, 53));
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutBtn.addActionListener(e -> handleLogout());

        userPanel.add(welcomeLabel);
        userPanel.add(logoutBtn);
        headerPanel.add(userPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Navigation Panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        navPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] navButtons = {"🚗 Browse Cars", "📋 My Bookings", "👤 Profile", "ℹ️ About"};
        int[] navActions = {1, 2, 3, 4};

        for (int i = 0; i < navButtons.length; i++) {
            RoundedButton btn = new RoundedButton(navButtons[i], ColorScheme.BUTTON_DEFAULT,
                    ColorScheme.BUTTON_HOVER, ColorScheme.BUTTON_PRESSED);
            int action = navActions[i];
            btn.addActionListener(e -> navigateTo(action));
            navPanel.add(btn);
        }

        mainPanel.add(navPanel, BorderLayout.SOUTH);

        // Content Panel
        contentPanel = new JPanel();
        contentPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        contentPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Initial view
        showBrowseCars();

        add(mainPanel);
    }

    private void navigateTo(int action) {
        contentPanel.removeAll();
        
        switch (action) {
            case 1:
                showBrowseCars();
                break;
            case 2:
                showMyBookings();
                break;
            case 3:
                showProfile();
                break;
            case 4:
                showAbout();
                break;
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showBrowseCars() {
        JPanel browsePanel = new JPanel(new BorderLayout());
        browsePanel.setBackground(ColorScheme.BACKGROUND_DARK);
        browsePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel browseTitle = new JLabel("🏎️ Available Luxury Cars");
        browseTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        browseTitle.setForeground(ColorScheme.ACCENT_GOLD);
        browsePanel.add(browseTitle, BorderLayout.NORTH);

        JPanel carsGridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        carsGridPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        carsGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        var availableCars = database.getAvailableCars();
        
        for (Car car : availableCars) {
            JPanel carCard = createCarCard(car);
            carsGridPanel.add(carCard);
        }

        JScrollPane scrollPane = new JScrollPane(carsGridPanel);
        scrollPane.setBackground(ColorScheme.BACKGROUND_DARK);
        scrollPane.getViewport().setBackground(ColorScheme.BACKGROUND_DARK);
        scrollPane.setBorder(null);

        browsePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(browsePanel, BorderLayout.CENTER);
    }

    private JPanel createCarCard(Car car) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(ColorScheme.PRIMARY_DARK);
        card.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER_COLOR, 2));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(ColorScheme.PRIMARY_DARK);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel carNameLabel = new JLabel(car.getBrand() + " " + car.getModel());
        carNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        carNameLabel.setForeground(ColorScheme.ACCENT_GOLD);

        JLabel carTypeLabel = new JLabel("Type: " + car.getCarType() + " | Year: " + car.getYear());
        carTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        carTypeLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        JLabel priceLabel = new JLabel("₹" + String.format("%.2f", car.getPricePerDay()) + " per day");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(ColorScheme.SUCCESS_GREEN);

        JLabel colorLabel = new JLabel("Color: " + car.getColor() + " | License: " + car.getLicensePlate());
        colorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        colorLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        infoPanel.add(carNameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(carTypeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(colorLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        RoundedButton bookBtn = new RoundedButton("BOOK NOW", ColorScheme.ACCENT_GOLD,
                new Color(255, 211, 20), new Color(255, 179, 0));
        bookBtn.addActionListener(e -> openBookingFrame(car));
        buttonPanel.add(bookBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void openBookingFrame(Car car) {
        new BookingFrame(customer, car, this);
    }

    private void showMyBookings() {
        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📋 My Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.ACCENT_GOLD);
        bookingPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel bookingsListPanel = new JPanel();
        bookingsListPanel.setLayout(new BoxLayout(bookingsListPanel, BoxLayout.Y_AXIS));
        bookingsListPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        bookingsListPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        var bookings = database.getCustomerBookings(customer.getCustomerId());
        if (bookings.isEmpty()) {
            JLabel noBookingsLabel = new JLabel("No bookings yet. Start exploring!");
            noBookingsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noBookingsLabel.setForeground(ColorScheme.TEXT_SECONDARY);
            bookingsListPanel.add(noBookingsLabel);
        } else {
            for (Booking booking : bookings) {
                Car car = database.getCarById(booking.getCarId());
                JPanel bookingCard = createBookingCard(booking, car);
                bookingsListPanel.add(bookingCard);
                bookingsListPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(bookingsListPanel);
        scrollPane.setBackground(ColorScheme.BACKGROUND_DARK);
        scrollPane.getViewport().setBackground(ColorScheme.BACKGROUND_DARK);
        scrollPane.setBorder(null);

        bookingPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(bookingPanel, BorderLayout.CENTER);
    }

    private JPanel createBookingCard(Booking booking, Car car) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(ColorScheme.PRIMARY_DARK);
        card.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER_COLOR, 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(ColorScheme.PRIMARY_DARK);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel bookingIdLabel = new JLabel("Booking #" + booking.getBookingId() + " - " + 
                car.getBrand() + " " + car.getModel());
        bookingIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookingIdLabel.setForeground(ColorScheme.ACCENT_GOLD);

        JLabel dateLabel = new JLabel("From: " + booking.getStartDate() + " To: " + booking.getEndDate() + 
                " (" + booking.getDays() + " days)");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        JLabel costLabel = new JLabel("Total Cost: ₹" + String.format("%.2f", booking.getTotalCost()));
        costLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        costLabel.setForeground(ColorScheme.SUCCESS_GREEN);

        JLabel statusLabel = new JLabel("Status: " + booking.getStatus().toUpperCase());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(getStatusColor(booking.getStatus()));

        infoPanel.add(bookingIdLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(costLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(statusLabel);

        card.add(infoPanel, BorderLayout.CENTER);
        return card;
    }

    private Color getStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "confirmed" -> ColorScheme.SUCCESS_GREEN;
            case "pending" -> ColorScheme.STATUS_PENDING;
            case "cancelled" -> ColorScheme.ERROR_RED;
            default -> ColorScheme.TEXT_SECONDARY;
        };
    }

    private void showProfile() {
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(ColorScheme.BACKGROUND_DARK);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("👤 My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.ACCENT_GOLD);
        profilePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 20, 15));
        infoPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[][] info = {
            {"Full Name:", customer.getName()},
            {"Email:", customer.getEmail()},
            {"Phone:", customer.getPhone()},
            {"License Number:", customer.getLicenseNumber()},
            {"Address:", customer.getAddress()},
            {"Member ID:", String.valueOf(customer.getCustomerId())}
        };

        for (String[] pair : info) {
            JLabel keyLabel = new JLabel(pair[0]);
            keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            keyLabel.setForeground(ColorScheme.ACCENT_GOLD);

            JLabel valueLabel = new JLabel(pair[1]);
            valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valueLabel.setForeground(ColorScheme.TEXT_PRIMARY);

            infoPanel.add(keyLabel);
            infoPanel.add(valueLabel);
        }

        profilePanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(profilePanel, BorderLayout.CENTER);
    }

    private void showAbout() {
        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.setBackground(ColorScheme.BACKGROUND_DARK);
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ℹ️ About Luxury Rentals");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.ACCENT_GOLD);
        aboutPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea aboutText = new JTextArea();
        aboutText.setText("Welcome to Luxury Rentals!\n\n" +
                "We provide premium luxury car rental services with a fleet of the finest vehicles.\n\n" +
                "Our Services:\n" +
                "✓ Exotic Car Rentals (Ferrari, Lamborghini, Porsche)\n" +
                "✓ Premium Sedans (Mercedes-Benz, BMW, Audi)\n" +
                "✓ Luxury SUVs (Range Rover, Audi Q7)\n" +
                "✓ Professional Service & Support\n" +
                "✓ Competitive Pricing & Special Offers\n\n" +
                "Contact us: support@luxuryrentals.com\n" +
                "Phone: 1-800-LUXURY-1\n\n" +
                "Thank you for choosing us!");
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aboutText.setForeground(ColorScheme.TEXT_PRIMARY);
        aboutText.setBackground(ColorScheme.PRIMARY_DARK);
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setBackground(ColorScheme.BACKGROUND_DARK);
        scrollPane.getViewport().setBackground(ColorScheme.PRIMARY_DARK);

        aboutPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(aboutPanel, BorderLayout.CENTER);
    }

    private void handleLogout() {
        dispose();
        new LoginFrame();
    }
}
