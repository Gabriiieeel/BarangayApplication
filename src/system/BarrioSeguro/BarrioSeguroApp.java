package system.BarrioSeguro;

import javax.swing.SwingUtilities;

public class BarrioSeguroApp {

    /**
     * The single main() entry point that starts the entire application.
     * Former “main” methods scattered across multiple classes are removed.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the application “controller”
            BarrioSeguro controller = new BarrioSeguro();
            // Start the app, which first displays the LoginForm
            controller.startApplication();
        });
    }
}