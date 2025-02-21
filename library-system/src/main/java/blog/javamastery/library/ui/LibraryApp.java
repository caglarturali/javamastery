package blog.javamastery.library.ui;

import blog.javamastery.library.ui.util.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryApp extends JFrame {
    private final FontManager fontManager;

    public LibraryApp() {
        fontManager = new FontManager(14, this::updateUI);

        initializeLookAndFeel();
        initializeFrame();

        JPanel mainPanel = createMainPanel();
        JMenuBar menuBar = createMenuBar();

        setJMenuBar(menuBar);
        add(mainPanel);

        // Center on screen
        setLocationRelativeTo(null);
    }

    private void initializeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeFrame() {
        // Basic window setup
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        addMenuItem("Exit", this::handleExit, fileMenu);

        JMenu viewMenu = new JMenu("View");
        addMenuItem("Increase Font Size", e -> fontManager.adjustSize(+1), viewMenu);
        addMenuItem("Decrease Font Size", e -> fontManager.adjustSize(-1), viewMenu);
        addMenuItem("Reset Font Size", e -> fontManager.resetSize(), viewMenu);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    private void addMenuItem(String text, ActionListener listener, JMenu menuToAdd) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        menuToAdd.add(menuItem);
    }

    private JPanel createMainPanel() {
        // Main content panel with some padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add Book");
        JButton searchButton = new JButton("Search");

        addButton.addActionListener(this::handleAddBook);
        searchButton.addActionListener(this::handleSearch);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        return buttonPanel;
    }

    private void handleExit(ActionEvent e) {
        System.exit(0);
    }

    private void handleAddBook(ActionEvent e) {
        System.out.println("Add clicked");
    }

    private void handleSearch(ActionEvent e) {
        System.out.println("Search clicked");
    }

    private void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
        invalidate();
        validate();
        repaint();
    }

    public static void main(String[] args) {
        // Ensure UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            app.setVisible(true);
        });
    }
}
