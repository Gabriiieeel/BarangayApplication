package system.BarrioSeguro;

import javax.swing.SwingUtilities;

public class BarrioSeguroApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BarrioSeguro appController = new BarrioSeguro();
            appController.startApplication();
        });
    }
}