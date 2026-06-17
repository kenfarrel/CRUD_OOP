package com.mycompany.javacrud;

import javax.swing.*;

/**
 * Entry point for the JavaCRUD application.
 * Run this class in IntelliJ via the green ▶ button or Shift+F10.
 * Debug via the bug icon or Shift+F9.
 */
public class Main {

    public static void main(String[] args) {
        // Apply Nimbus look-and-feel if available
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            // Fall back to default L&F — not fatal
            System.err.println("Nimbus L&F not available, using default: " + ex.getMessage());
        }

        // All Swing work must happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            FormSiswa frame = new FormSiswa();
            frame.setVisible(true);
        });
    }
}