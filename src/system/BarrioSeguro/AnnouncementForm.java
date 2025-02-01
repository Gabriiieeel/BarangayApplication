package system.BarrioSeguro;

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

public class AnnouncementForm extends BaseForm {

    private JTextField subjectTextField;
    private JTextArea messageTextField;

    public AnnouncementForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Announcements");
        initialize();
    }

    private void initialize() {
        JLayeredPane announcePane = new JLayeredPane();
        setContentPane(announcePane);

        addBackgroundImage(announcePane);
        addDashboardPanel(announcePane);
        addAnnouncePanel(announcePane);
    }

    private void addAnnouncePanel(JLayeredPane announcePane) {
        JPanel announcePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        announcePanel.setBounds(480, 185, 895, 640);
        announcePanel.setBackground(new Color(102, 77, 77, 178));  // Semi-transparent background
        announcePanel.setLayout(null);
        announcePanel.setOpaque(false);

        announcePane.add(announcePanel, JLayeredPane.PALETTE_LAYER);

        subjectTextField = createRoundedTextField("Subject", 25);
        subjectTextField.setToolTipText("");
        subjectTextField.setHorizontalAlignment(SwingConstants.LEFT);
        subjectTextField.setForeground(new Color(0,0,0,50));
        subjectTextField.setBackground(new Color(255, 244, 244));
        subjectTextField.setFont(new Font("SansSerif", Font.PLAIN, 25));
        subjectTextField.setBorder(new EmptyBorder(10, 20, 10, 20));
        subjectTextField.setBounds(51, 50, 798, 53);

        subjectTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForSubjectField) {
                if (subjectTextField.getText().equals("Subject")) {
                    subjectTextField.setText("");
                    subjectTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent eventForSubjectField) {
                if (subjectTextField.getText().isEmpty()) {
                    subjectTextField.setText("Subject");
                    subjectTextField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        announcePanel.add(subjectTextField);

        // Message input field with placeholder functionality
        messageTextField = createRoundedTextArea("Enter a message...", 798, 323);
        announcePanel.add(messageTextField);

        // Button to send email
        JButton emailBtn = new JButton("Send to Email");
        styleRoundedButton(emailBtn);
        emailBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForEmailBtn) {
                if (validateFields()) {
                    String subject = subjectTextField.getText();
                    String messageContent = messageTextField.getText();
                    sendEmail(subject, messageContent);
                }
            }
        });
        emailBtn.setBounds(555, 497, 177, 55);
        announcePanel.add(emailBtn);

        // Button to send SMS (Not implemented, placeholder only)
        JButton smsBtn = new JButton("Send to SMS");
        styleRoundedButton(smsBtn);
        smsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSMSbtn) {
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

    private void sendEmail(String subject, String messageContent) {
    	// Sends email to all residents using stored emails from the database
        final String username = "gabdelacruz926@gmail.com";
        final String password = "wnvmmbowuvxtbbvr";

        Properties propsGmailServer = new Properties();
        propsGmailServer.put("mail.smtp.auth", "true");
        propsGmailServer.put("mail.smtp.starttls.enable", "true");
        propsGmailServer.put("mail.smtp.host", "smtp.gmail.com");
        propsGmailServer.put("mail.smtp.port", "587");

        Authenticator emailAuthenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session sessionForEmail = Session.getInstance(propsGmailServer, emailAuthenticator);

        try {
            Connection connectSendEmail = getConnection();
            String query = "SELECT resident_email FROM ResidentDB";
            Statement statementSendEmail = connectSendEmail.createStatement();
            ResultSet resultSendEmail = statementSendEmail.executeQuery(query);

            StringBuilder listEmailAddress = new StringBuilder();

            while (resultSendEmail.next()) {
                String email = resultSendEmail.getString("resident_email");
                listEmailAddress.append(email).append(",");
            }

            if (listEmailAddress.length() > 0) {
                listEmailAddress.setLength(listEmailAddress.length() - 1);
            }

            Message createNewMessage = new MimeMessage(sessionForEmail);
            createNewMessage.setFrom(new InternetAddress(username));
            createNewMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(listEmailAddress.toString()));
            createNewMessage.setSubject(subject);
            createNewMessage.setText(messageContent);

            Transport.send(createNewMessage);
            
            subjectTextField.setText("");
            messageTextField.setText("");
            
            resultSendEmail.close();
            statementSendEmail.close();
            connectSendEmail.close();
            
            JOptionPane.showMessageDialog(AnnouncementForm.this, "Emails sent successfully!");

        } catch (Exception handleEmailException) {
            handleEmailException.printStackTrace();
            JOptionPane.showMessageDialog(AnnouncementForm.this, "Failed to send emails. " + handleEmailException.getMessage());
        }
    }

    private void sendSMS(String subject, String messageContent) {
        /* Assume that we send the SMS to the residents */
        /* Paid SMS API is recommended. */
        subjectTextField.setText("");
        messageTextField.setText("");

        JOptionPane.showMessageDialog(AnnouncementForm.this, "SMS sent successfully!");
    }
}
