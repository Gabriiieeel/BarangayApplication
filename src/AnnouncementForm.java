import java.awt.*;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import javax.mail.Authenticator;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AnnouncementForm {

    public JFrame frame;
    private JTextField subjecttf;
    private JTextArea messagetf;
    private static final String DB_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HomepageForm window = new HomepageForm();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @wbp.parser.entryPoint
     */
    public AnnouncementForm() {
        initialize();
    }

    /**
     * @wbp.parser.entryPoint
     */

    private void initialize() {
        frame = new JFrame("BarrioSeguro-Announcements");
        frame.setBounds(100, 100, 1440, 1024); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1440, 1024);
        frame.setContentPane(layeredPane);

        addBackgroundImage(layeredPane);

        addDashboardPanel(layeredPane);

        addWelcomePanel(layeredPane);
    }

    private void addBackgroundImage(JLayeredPane layeredPane) {
        //background image
        ImageIcon backgroundImage = new ImageIcon("Visuals/bg.png");

        JLabel backgroundLabel = new JLabel(backgroundImage) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the image stretched to fit the panel size
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundLabel.setBounds(0, 0, 1440, 1024); 
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER); 
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

            JOptionPane.showMessageDialog(frame, "Emails sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to send emails. " + e.getMessage());
        }
    }


    private void addDashboardPanel(JLayeredPane layeredPane) {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(new Color(102, 77, 77, 178)); // Set red background
        dashboardPanel.setBounds(0, 0, 430, 1024); // Set width to 430 and height to 1024
        layeredPane.add(dashboardPanel, JLayeredPane.PALETTE_LAYER);

        // Add logo inside the DashboardPanel
        addLogoToDashboard(dashboardPanel);

        // Add "BarrioSeguro" text inside the DashboardPanel
        addTextToDashboard(dashboardPanel);

        // Buttons inside the dashboard panel
        addDashboardButtons(dashboardPanel);
    }

    private void addLogoToDashboard(JPanel dashboardPanel) {
        ImageIcon logoImage = new ImageIcon("Visuals/logo.png");
        JLabel logoLabel = new JLabel(logoImage);
        logoLabel.setBounds(10, 33, 150, 150); 
        dashboardPanel.setLayout(null); 
        dashboardPanel.add(logoLabel);
    }

    private void addTextToDashboard(JPanel dashboardPanel) {
        JLabel logonamelabel = new JLabel("BarrioSeguro");
        logonamelabel.setForeground(Color.WHITE);
        logonamelabel.setFont(new Font("Times New Roman", Font.BOLD, 39)); 
        logonamelabel.setBounds(170, 69, 237, 78); 
        dashboardPanel.add(logonamelabel);
    }

    private void addDashboardButtons(JPanel dashboardPanel) {
    	JButton announceBtn = new JButton("Announcement");
    	announceBtn.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        // Open the AnnouncementForm
    	        EventQueue.invokeLater(new Runnable() {
    	            public void run() {
    	                try {
    	                    AnnouncementForm announcementForm = new AnnouncementForm();
    	                    announcementForm.frame.setVisible(true); // Make the AnnouncementForm visible
    	                } catch (Exception ex) {
    	                    ex.printStackTrace();
    	                }
    	                frame.dispose();
    	            }
    	        });
    	    }
    	});
    	announceBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
    	announceBtn.setBounds(0, 250, 430, 78);
    	dashboardPanel.add(announceBtn);

        announceBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        announceBtn.setBounds(0, 250, 430, 78);
        dashboardPanel.add(announceBtn);

        JButton resdatBtn = new JButton("Resident Database");
        resdatBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
      		  EventQueue.invokeLater(new Runnable() {
    	            public void run() {
    	                try {
    	                    ResidentForm residentform = new ResidentForm();
    	                  residentform.frame.setVisible(true); // Make the residentform visible
    	                } catch (Exception ex) {
    	                    ex.printStackTrace();
    	                }
    	                frame.dispose();
    	            }
    	        });
    	    }
        });
        resdatBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        resdatBtn.setBounds(0, 371, 430, 78);
        dashboardPanel.add(resdatBtn);

        JButton crimeBtn = new JButton("Incident Reports");
        crimeBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  EventQueue.invokeLater(new Runnable() {
      	            public void run() {
      	                try {
      	                    CrimeForm crimeform = new CrimeForm();
      	                    crimeform.frame.setVisible(true); // Make the residentform visible
      	                } catch (Exception ex) {
      	                    ex.printStackTrace();
      	                }
      	                frame.dispose();
      	            }
      	        });
      	    }
        });
        crimeBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        crimeBtn.setBounds(0, 489, 430, 78);
        dashboardPanel.add(crimeBtn);

        JButton summBtn = new JButton("Summary Reports");
        summBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
      		  EventQueue.invokeLater(new Runnable() {
    	            public void run() {
    	                try {
    	                    SummaryForm summaryform = new SummaryForm();
    	                  summaryform.frame.setVisible(true); // Make the residentform visible
    	                } catch (Exception ex) {
    	                    ex.printStackTrace();
    	                }
    	                frame.dispose();
    	            }
    	        });
    	    }
        });
        summBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        summBtn.setBounds(0, 611, 430, 78);
        dashboardPanel.add(summBtn);

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the current HomepageForm
                frame.dispose();
                
                // Open the LoginForm again
                EventQueue.invokeLater(() -> {
                    LoginForm loginWindow = new LoginForm(); // Create a new instance of LoginForm
                    loginWindow.frame.setVisible(true); // Show the LoginForm
                });
            }
        });

        logoutBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        logoutBtn.setBounds(135, 805, 150, 55);
        dashboardPanel.add(logoutBtn);
    }
}
