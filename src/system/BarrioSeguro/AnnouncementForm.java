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

import javax.mail.Authenticator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;

import java.util.Properties;

/**
 * Handles announcements creation or display.
 * You may have repeated code for sending emails or saving to DB.
 */
public class AnnouncementForm extends BaseForm {

    private JTextField subjecttf;
    private JTextArea messagetf;

    public AnnouncementForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Announcements");
        initialize();
    }

    private void initialize() {
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        addBackgroundImage(layeredPane);

        addDashboardPanel(layeredPane);

        addWelcomePanel(layeredPane);
    }

    private void addWelcomePanel(JLayeredPane layeredPane) {
      // Use JPanel instead of Panel
      JPanel welcomePanel = new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2d = (Graphics2D) g;
              g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              g2d.setColor(getBackground());
              g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
          }
      };
      welcomePanel.setBounds(480, 185, 895, 640);
      welcomePanel.setBackground(new Color(102, 77, 77, 178)); 
      welcomePanel.setLayout(null);
      welcomePanel.setOpaque(false);

      layeredPane.add(welcomePanel, JLayeredPane.PALETTE_LAYER);

      // For the subject text field
      subjecttf = new JTextField("Subject");  // Set the placeholder
      subjecttf.setToolTipText("");
      subjecttf.setHorizontalAlignment(SwingConstants.LEFT);
      subjecttf.setForeground(Color.LIGHT_GRAY);
      subjecttf.setFont(new Font("SansSerif", Font.PLAIN, 25));
      subjecttf.setBorder(new EmptyBorder(10, 20, 10, 20));
      subjecttf.setBounds(51, 50, 798, 53);

      subjecttf.addFocusListener(new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
              if (subjecttf.getText().equals("Subject")) {  // Check if placeholder is visible
                  subjecttf.setText("");  // Clear the placeholder text
                  subjecttf.setForeground(Color.BLACK);  // Set text color to black when typing
              }
          }

          @Override
          public void focusLost(FocusEvent e) {
              if (subjecttf.getText().isEmpty()) {  // If the text field is empty
                  subjecttf.setText("Subject");  // Restore the placeholder text
                  subjecttf.setForeground(Color.LIGHT_GRAY);  // Set text color back to light gray
              }
          }
      });

      welcomePanel.add(subjecttf);

   // For messagetf (JTextArea) with a placeholder
      messagetf = new JTextArea("Enter a message...") {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              if (getText().isEmpty() && getForeground() == Color.LIGHT_GRAY) {
                  // Draw the placeholder at the top-left corner (x=10, y=20 for example)
                  g.setColor(getForeground());
                  g.drawString("Enter a message...", 10, 20); 
              }
          }
      };
      messagetf.setFont(new Font("SansSerif", Font.PLAIN, 14));
      messagetf.setForeground(Color.LIGHT_GRAY); // Light gray color for placeholder
      messagetf.setBounds(51, 136, 798, 323);
      messagetf.setWrapStyleWord(true);
      messagetf.setLineWrap(true);

      // Set margin
      messagetf.setMargin(new Insets(20, 20, 20, 20));

      // Add FocusListener to handle placeholder behavior
      messagetf.addFocusListener(new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
              if (messagetf.getText().equals("Enter a message...")) { 
                  messagetf.setText("");  // Clear the placeholder text
                  messagetf.setForeground(Color.BLACK);  // Set text color to black when typing
              }
          }

          @Override
          public void focusLost(FocusEvent e) {
              if (messagetf.getText().isEmpty()) {  
                  messagetf.setText("Enter a message..."); 
                  messagetf.setForeground(Color.LIGHT_GRAY);  
              }
          }
      });

      welcomePanel.add(messagetf);
      
      JButton emailbtn = new JButton("Send to Email");
      emailbtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String subject = subjecttf.getText(); // Get the subject from subjecttf
              String messageContent = messagetf.getText(); // Get the message from messagetf

              // Send email to all emails from the database
              sendEmail(subject, messageContent);
          }
      });
      emailbtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
      emailbtn.setBounds(569, 497, 177, 55);
      welcomePanel.add(emailbtn);
      
      JButton smsbtn = new JButton("Send to SMS");
      smsbtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
      smsbtn.setBounds(181, 497, 177, 55);
      welcomePanel.add(smsbtn);
  }
  
  private void sendEmail(String subject, String messageContent) {
      final String username = "gabdelacruz926@gmail.com"; // Sender's email
      final String password = "wnvmmbowuvxtbbvr";

      // Set up properties for the SMTP server (Gmail)
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

      // Create an authenticator with the email credentials
      Authenticator authenticator = new Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(username, password);
          }
      };

      // Create the Session object
      Session session = Session.getInstance(props, authenticator);

      try {
          // Query the database for all resident emails
          Connection conn = DriverManager.getConnection("jdbc:ucanaccess://Database/BarrioSeguroDB.accdb");
          String query = "SELECT resident_email FROM ResidentDB";
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(query);

          // Create a list to store the email addresses
          StringBuilder emailAddresses = new StringBuilder();

          // Iterate over the result set and add each email to the list
          while (rs.next()) {
              String email = rs.getString("resident_email");
              emailAddresses.append(email).append(","); // Add email to the list
          }

          // Remove comma
          if (emailAddresses.length() > 0) {
              emailAddresses.setLength(emailAddresses.length() - 1);
          }

          // Create a new email message
          Message msg = new MimeMessage(session);
          msg.setFrom(new InternetAddress(username)); // Sender's email
          msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddresses.toString())); // Recipient's emails
          msg.setSubject(subject); // Set the subject
          msg.setText(messageContent); // Set the message content

          // Send the message
          Transport.send(msg);
          
          subjecttf.setText("");
          messagetf.setText("");
         

          // Close the resources
          rs.close();
          stmt.close();
          conn.close();

          JOptionPane.showMessageDialog(this, "Emails sent successfully!");

      } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(this, "Failed to send emails. " + e.getMessage());
      }
  }
}
