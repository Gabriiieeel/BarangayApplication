// This line places our file inside the "system.BarrioSeguro" package  
// A package is like a folder name to keep related classes together
package system.BarrioSeguro;

// These lines import the tools and libraries we need  
// They include colors, fonts, event listeners, database connections, and email utilities
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;

// "AnnouncementForm" is a class that extends "BaseForm"  
// "extends" means our class has the functions and properties from "BaseForm"
public class AnnouncementForm extends BaseForm {

    // We declare two input fields: one "JTextField" for the subject, one "JTextArea" for the message
    private JTextField subjectTextField;
    private JTextArea messageTextField;

    // This is the constructor for "AnnouncementForm"  
    // It sets the window title and calls the "initialize()" method
    public AnnouncementForm(BarrioSeguro appController) {
        // Gives the parent class (BaseForm) our application controller
        super(appController);
        setTitle("BarrioSeguro - Announcements");
        initialize();
    }

    // "initialize" sets up everything on the form: background, panels, etc.
    private void initialize() {
        // Create a layered pane, which lets us stack panels and background images
        JLayeredPane announcePane = new JLayeredPane();
        // Make this pane the main content area
        setContentPane(announcePane);

        // Adds a background image behind all other panels
        addBackgroundImage(announcePane);
        // Adds a side panel (dashboard) for navigation
        addDashboardPanel(announcePane);
        // Adds our main announcement panel for subject and message
        addAnnouncePanel(announcePane);
    }

    // "addAnnouncePanel" makes a special panel for writing announcements  
    // It uses a rounded rectangle and some transparent color
    private void addAnnouncePanel(JLayeredPane announcePane) {
        // Create a JPanel that draws rounded corners in "paintComponent"
        JPanel announcePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                // Turn on smoother edges
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fill this area with the panel’s background color
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        // Position the panel and give it a semi-transparent brownish background
        announcePanel.setBounds(480, 185, 895, 640);
        announcePanel.setBackground(new Color(102, 77, 77, 178));  // Semi-transparent background
        announcePanel.setLayout(null);
        announcePanel.setOpaque(false);

        // Place this panel in a higher layer than the background image
        announcePane.add(announcePanel, JLayeredPane.PALETTE_LAYER);

        // Now we build our subject text field
        subjectTextField = createRoundedTextField("Subject", 25);
        // "setToolTipText" can show a small note if the user hovers over the field
        subjectTextField.setToolTipText("");
        subjectTextField.setHorizontalAlignment(SwingConstants.LEFT);
        subjectTextField.setForeground(new Color(0,0,0,50));
        subjectTextField.setBackground(new Color(255, 244, 244));
        subjectTextField.setFont(new Font("SansSerif", Font.PLAIN, 25));
        subjectTextField.setBorder(new EmptyBorder(10, 20, 10, 20));
        subjectTextField.setBounds(51, 50, 798, 53);

        // We add "FocusListener" so that if the text says "Subject," it goes away when clicked
        subjectTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForSubjectField) {
                 // If it's still the default text, empty it and switch the color to black
                if (subjectTextField.getText().equals("Subject")) {
                    subjectTextField.setText("");
                    subjectTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent eventForSubjectField) {
                // If the user left the field empty, return the placeholder text
                if (subjectTextField.getText().isEmpty()) {
                    subjectTextField.setText("Subject");
                    subjectTextField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        // Add this subject field to our panel
        announcePanel.add(subjectTextField);

        // Build a text area for the message part, using a helper method
        messageTextField = createRoundedTextArea("Enter a message...", 798, 323);
        // Position it below the subject
        announcePanel.add(messageTextField);

         // Make a button for sending emails
        JButton emailBtn = new JButton("Send to Email");
        // Use our style for rounded buttons from "BaseForm"
        styleRoundedButton(emailBtn);
        // When clicked, check the fields, and if valid, send the email
        emailBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForEmailBtn) {
                if (validateFields()) {
                    String subject = subjectTextField.getText();
                    String messageContent = messageTextField.getText();
                    // Actually send the email with the typed subject and message
                    sendEmail(subject, messageContent);
                }
            }
        });
        // Position the button on the panel
        emailBtn.setBounds(555, 497, 177, 55);
        announcePanel.add(emailBtn);

        // Make a button that says "Send to SMS" (not fully implemented, just a placeholder)
        JButton smsBtn = new JButton("Send to SMS");
        styleRoundedButton(smsBtn);
        smsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSMSbtn) {
                // If our subject and message are okay, attempt an SMS send
                if (validateFields()) {
                    String subject = subjectTextField.getText();
                    String messageContent = messageTextField.getText();
                    sendSMS(subject, messageContent);
                }
            }
        });
        smsBtn.setBounds(167, 497, 177, 55);
        announcePanel.add(smsBtn);
    }

    // "validateFields" checks if "Subject" and "Message" fields are not empty  
    // If they are empty, we show an error and return false; otherwise, return true
    private boolean validateFields() {
    	// Ensures fields are not empty before sending email or SMS
        if (subjectTextField.getText().equals("Subject") || subjectTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(AnnouncementForm.this, "Subject is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (messageTextField.getText().equals("Enter a message...") || messageTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(AnnouncementForm.this, "Message is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // "sendEmail" sends an email to the addresses in our database of residents  
    // It uses the JavaMail library for sending email
    private void sendEmail(String subject, String messageContent) {
        // Our Gmail address and password for sending emails  
        // Must be valid, or the email won’t go through
        final String username = "gabdelacruz926@gmail.com";
        final String password = "wnvmmbowuvxtbbvr";

        // A set of properties that tell JavaMail how to talk to Gmail’s server
        Properties propsGmailServer = new Properties();
        propsGmailServer.put("mail.smtp.auth", "true");
        propsGmailServer.put("mail.smtp.starttls.enable", "true");
        propsGmailServer.put("mail.smtp.host", "smtp.gmail.com");
        propsGmailServer.put("mail.smtp.port", "587");

        // An "Authenticator" helps verify we have the correct username and password
        Authenticator emailAuthenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // A session is the connection to the mail server
        Session sessionForEmail = Session.getInstance(propsGmailServer, emailAuthenticator);

        try {
            // Open our database and find every resident’s email
            Connection connectSendEmail = getConnection();
            String query = "SELECT resident_email FROM ResidentDB";
            Statement statementSendEmail = connectSendEmail.createStatement();
            ResultSet resultSendEmail = statementSendEmail.executeQuery(query);

            // We gather all emails into a string separated by commas
            StringBuilder listEmailAddress = new StringBuilder();
            while (resultSendEmail.next()) {
                String email = resultSendEmail.getString("resident_email");
                listEmailAddress.append(email).append(",");
            }
            // Remove the last comma
            if (listEmailAddress.length() > 0) {
                listEmailAddress.setLength(listEmailAddress.length() - 1);
            }

            // "MimeMessage" is the actual email we’re sending
            Message createNewMessage = new MimeMessage(sessionForEmail);
            createNewMessage.setFrom(new InternetAddress(username));
            // "Message.RecipientType.TO" passes our list of emails
            createNewMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(listEmailAddress.toString()));
            createNewMessage.setSubject(subject);
            createNewMessage.setText(messageContent);

            // Send the email
            Transport.send(createNewMessage);
            
            // Clear out the fields after sending
            subjectTextField.setText("");
            messageTextField.setText("");
            
            // Close everything: result set, statement, connection
            resultSendEmail.close();
            statementSendEmail.close();
            connectSendEmail.close();
            
            // Show a success message
            JOptionPane.showMessageDialog(AnnouncementForm.this, "Emails sent successfully!");

        } catch (Exception handleEmailException) {
            // If something goes wrong, show an error
            handleEmailException.printStackTrace();
            JOptionPane.showMessageDialog(AnnouncementForm.this, "Failed to send emails. " + handleEmailException.getMessage());
        }
    }

    // "sendSMS" is a placeholder for sending text messages  
    // It just shows a success message for now
    private void sendSMS(String subject, String messageContent) {
        /* Assume that we send the SMS to the residents */
        // Clear the subject and message fields
        subjectTextField.setText("");
        messageTextField.setText("");

        // Show a dialog box that says it’s successful
        JOptionPane.showMessageDialog(AnnouncementForm.this, "SMS sent successfully!");
    }
}
