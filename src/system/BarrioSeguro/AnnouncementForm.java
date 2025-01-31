package system.BarrioSeguro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
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
        announcePanel.setBackground(new Color(102, 77, 77, 178)); 
        announcePanel.setLayout(null);
        announcePanel.setOpaque(false);

        announcePane.add(announcePanel, JLayeredPane.PALETTE_LAYER);

        subjectTextField = new JTextField("Subject");
        subjectTextField.setToolTipText("");
        subjectTextField.setHorizontalAlignment(SwingConstants.LEFT);
        subjectTextField.setForeground(Color.LIGHT_GRAY);
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

        messageTextField = new JTextArea("Enter a message...") {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                if (getText().isEmpty() && getForeground() == Color.LIGHT_GRAY) {
                    paintGraphics.setColor(getForeground());
                    paintGraphics.drawString("Enter a message...", 10, 20); 
                }
            }
        };
        messageTextField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageTextField.setForeground(Color.LIGHT_GRAY);
        messageTextField.setBounds(51, 136, 798, 323);
        messageTextField.setWrapStyleWord(true);
        messageTextField.setLineWrap(true);

        messageTextField.setMargin(new Insets(20, 20, 20, 20));

        messageTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForMessageField) {
                if (messageTextField.getText().equals("Enter a message...")) { 
                    messageTextField.setText("");
                    messageTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent eventForMessageField) {
                if (messageTextField.getText().isEmpty()) {  
                    messageTextField.setText("Enter a message..."); 
                    messageTextField.setForeground(Color.LIGHT_GRAY);  
                }
            }
        });

        announcePanel.add(messageTextField);
        
        JButton emailBtn = new JButton("Send to Email");
        emailBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForEmailBtn) {
                String subject = subjectTextField.getText();
                String messageContent = messageTextField.getText();

                sendEmail(subject, messageContent);
            }
        });
        emailBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        emailBtn.setBounds(569, 497, 177, 55);
        announcePanel.add(emailBtn);
        
        JButton smsBtn = new JButton("Send to SMS");
        smsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSMSbtn) {
                String subject = subjectTextField.getText();
                String messageContent = messageTextField.getText();

                sendSMS(subject, messageContent);
            }
        });
        smsBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        smsBtn.setBounds(181, 497, 177, 55);
        announcePanel.add(smsBtn);
    }

    private void sendEmail(String subject, String messageContent) {
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
