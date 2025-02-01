// This statement groups our code in a folder-like structure, called a "package"  
// Package means we are organizing files so they are easier to find
package system.BarrioSeguro;

// This line tells our program to use part of Java called "SwingUtilities"  
// "SwingUtilities" is a helper that runs visual actions safely inside the program
import javax.swing.SwingUtilities;

// This says we are creating a class named "BarrioSeguroApp"  
// A class is like a blueprint; it holds our code
public class BarrioSeguroApp {

    // This function is "main", the first thing the program runs  
    // "main" is where the software starts working when we open it
    public static void main(String[] args) {

        // We use "SwingUtilities.invokeLater" to schedule our code for the "Event Dispatch Thread"  
        // In simple words, it means: run the next part safely for the visual part of our program
        SwingUtilities.invokeLater(() -> {

            // We create a new object named "appController" from the "BarrioSeguro" class  
            // "BarrioSeguro" is another part of our program that does the main tasks
            BarrioSeguro appController = new BarrioSeguro();

            // Here we call the "startApplication" method on our "appController"  
            // This means: "Begin the program and show any interface or work we need"
            appController.startApplication();
        });
    }
}