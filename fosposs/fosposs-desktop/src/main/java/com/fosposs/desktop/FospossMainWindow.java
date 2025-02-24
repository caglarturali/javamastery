package com.fosposs.desktop;

import javax.swing.*;
import java.awt.*;

public class FospossMainWindow extends JFrame {
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JPanel statusBar;
    private JLabel statusLabel;

    @SuppressWarnings("CallToPrintStackTrace")
    public FospossMainWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Fosposs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));

        // Add main panel to the frame
        add(mainPanel);

        // Increase toolbar button size
        Font buttonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 36);
        for (Component c : toolBar.getComponents()) {
            if (c instanceof JButton) {
                c.setFont(buttonFont);
            }
        }

        setLocationRelativeTo(null);
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(" " + message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FospossMainWindow window = new FospossMainWindow();
            window.setVisible(true);
        });
    }
}