package system.BarrioSeguro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * For managing incident/crime reports: read, write, update, etc.
 */
public class CrimeForm extends BaseForm {

    private JTextField incFName;
    private JTextField incMidName;
    private JTextField incLName;
    private JTextField incSufName;
    private JTextField incDate;
    private JComboBox<String> incType;
    private JTextArea incDescrip;
    private JComboBox<String> incProg;

    // If you had more fields, declare them all here

    public CrimeForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Incident Reports");
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
      welcomePanel.setBounds(480, 185, 895, 644);
      welcomePanel.setBackground(new Color(102, 77, 77, 178));
      welcomePanel.setLayout(null);
      welcomePanel.setOpaque(false);
      layeredPane.add(welcomePanel, JLayeredPane.PALETTE_LAYER);
      
      incFName = new JTextField("");
      incFName.setToolTipText("");
      incFName.setHorizontalAlignment(SwingConstants.LEFT);
      incFName.setForeground(new Color(0, 0, 0));
      incFName.setFont(new Font("SansSerif", Font.PLAIN, 12));
      incFName.setBorder(new EmptyBorder(10, 10, 10, 10));
      incFName.setBounds(40, 80, 216, 36);
      
      welcomePanel.add(incFName);
      
      incMidName = new JTextField("");
      incMidName.setToolTipText("");
      incMidName.setHorizontalAlignment(SwingConstants.LEFT);
      incMidName.setForeground(new Color(0, 0, 0));
      incMidName.setFont(new Font("SansSerif", Font.PLAIN, 12));
      incMidName.setBorder(new EmptyBorder(10, 10, 10, 10));
      incMidName.setBounds(40, 142, 216, 36);
      welcomePanel.add(incMidName);
      
      incLName = new JTextField("");
      incLName.setToolTipText("");
      incLName.setHorizontalAlignment(SwingConstants.LEFT);
      incLName.setForeground(new Color(0, 0, 0));
      incLName.setFont(new Font("SansSerif", Font.PLAIN, 12));
      incLName.setBorder(new EmptyBorder(10, 10, 10, 10));
      incLName.setBounds(40, 204, 216, 36);
      welcomePanel.add(incLName);
      
      incSufName = new JTextField("");
      incSufName.setToolTipText("");
      incSufName.setHorizontalAlignment(SwingConstants.LEFT);
      incSufName.setForeground(new Color(0, 0, 0));
      incSufName.setFont(new Font("SansSerif", Font.PLAIN, 12));
      incSufName.setBorder(new EmptyBorder(10, 10, 10, 10));
      incSufName.setBounds(40, 266, 216, 36);
      welcomePanel.add(incSufName);
      
      incDate = new JTextField("");
      incDate.setToolTipText("");
      incDate.setHorizontalAlignment(SwingConstants.LEFT);
      incDate.setForeground(new Color(0, 0, 0));
      incDate.setFont(new Font("SansSerif", Font.PLAIN, 12));
      incDate.setBorder(new EmptyBorder(10, 10, 10, 10));
      incDate.setBounds(40, 328, 216, 36);
      welcomePanel.add(incDate);
      
      incType = new JComboBox<String>();
      incType.setModel(new DefaultComboBoxModel<String>(new String[] {"Assault", "Burglary", "Domestic Violence", "Drug Possession", "Fraud", "Harassment", "Physical Abuse", "Theft", "Traffic Violation", "Vandalism", "Arson", "Robbery", "Homicide", "Kidnapping", "Shoplifting", "Identity Theft", "Embezzlement", "Money Laundering", "Stalking", "Sexual Assault", "Child Abuse", "Human Trafficking", "Battery", "Bribery", "Extortion", "Counterfeiting", "Piracy", "Public Intoxication", "Rape", "Prostitution", "Weapons Possession", "Animal Cruelty", "Cybercrime", "Blackmail", "Tax Evasion", "Illegal Gambling", "Wire Fraud", "Trespassing", "Insurance Fraud", "Unlawful Detention", "Obstruction of Justice", "Racketeering", "Manslaughter", "Money Counterfeiting", "Corruption", "Illegal Search and Seizure", "Forgery", "Drug Trafficking", "Terrorism", "Public Disorder", "Bribing a Witness", "Reckless Driving", "Destruction of Property", "Coercion"}));
      incType.setBounds(40, 390, 216, 36);
      welcomePanel.add(incType);
      
      incProg = new JComboBox<String>();
      incProg.setModel(new DefaultComboBoxModel<String>(new String[] {"Under Investigation", "Ongoing", "Resolved", "Closed"}));
      incProg.setBounds(40, 452, 216, 36);
      welcomePanel.add(incProg);
      
      incDescrip = new JTextArea("Enter a message...") {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              if (getText().isEmpty() && getForeground() == Color.LIGHT_GRAY) {
                  g.setColor(getForeground());
                  g.drawString("Enter a message...", 10, 20);
              }
          }
      };
      incDescrip.setFont(new Font("SansSerif", Font.PLAIN, 14));
      incDescrip.setForeground(new Color(0, 0, 0));
      incDescrip.setWrapStyleWord(true);
      incDescrip.setBounds(273, 65, 583, 423);
      incDescrip.setLineWrap(true);
    
      // Set margin padding in tf
      incDescrip.setMargin(new Insets(20, 20, 20, 20));
      //for placeholder
      incDescrip.addFocusListener(new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
              if (incDescrip.getText().equals("Enter a message...")) {
                incDescrip.setText(""); 
                incDescrip.setForeground(Color.BLACK); 
              }
          }

          @Override
          public void focusLost(FocusEvent e) {
              if (incDescrip.getText().isEmpty()) {
                incDescrip.setText("Enter a message..."); 
                incDescrip.setForeground(Color.LIGHT_GRAY); 
              }
          }
      });
      welcomePanel.add(incDescrip);
      
      JLabel lblNewLabel = new JLabel("Enter First Name");
      lblNewLabel.setForeground(new Color(255, 255, 255));
      lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblNewLabel.setBounds(40, 65, 118, 19);
      welcomePanel.add(lblNewLabel);
      
      JLabel lblEnterMiddleName = new JLabel("Enter Middle Name (empty if none)");
      lblEnterMiddleName.setForeground(new Color(255, 255, 255));
      lblEnterMiddleName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblEnterMiddleName.setBounds(40, 127, 216, 18);
      welcomePanel.add(lblEnterMiddleName);
      
      JLabel lblEnterLastName = new JLabel("Enter Last Name");
      lblEnterLastName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblEnterLastName.setForeground(new Color(255, 255, 255));
      lblEnterLastName.setBounds(40, 189, 184, 18);
      welcomePanel.add(lblEnterLastName);
      
      JLabel lblEnterSuffixempty = new JLabel("Enter Suffix (empty if none)");
      lblEnterSuffixempty.setForeground(new Color(255, 255, 255));
      lblEnterSuffixempty.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblEnterSuffixempty.setBounds(40, 251, 184, 18);
      welcomePanel.add(lblEnterSuffixempty);
      
      JLabel lblEnterDate = new JLabel("Enter Date: DD/MM/YYYY");
      lblEnterDate.setForeground(new Color(255, 255, 255));
      lblEnterDate.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblEnterDate.setBounds(40, 313, 184, 18);
      welcomePanel.add(lblEnterDate);
      
      JLabel lblChooseAType = new JLabel("Choose a type of Incident");
      lblChooseAType.setForeground(new Color(255, 255, 255));
      lblChooseAType.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblChooseAType.setBounds(40, 375, 184, 21);
      welcomePanel.add(lblChooseAType);
      
      JLabel lblIncidentProgress = new JLabel("Incident Progress");
      lblIncidentProgress.setForeground(new Color(255, 255, 255));
      lblIncidentProgress.setFont(new Font("Times New Roman", Font.PLAIN, 11));
      lblIncidentProgress.setBounds(40, 437, 184, 18);
      welcomePanel.add(lblIncidentProgress);
      
      JButton submitbtn = new JButton("Submit");
      submitbtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
      submitbtn.setBounds(378, 529, 150, 55);
      submitbtn.addActionListener(_ -> submitIncident());
      welcomePanel.add(submitbtn);
  }
  
  private boolean validateFormFields() {
      if (incFName.getText().isEmpty() || incLName.getText().isEmpty() || incDate.getText().isEmpty()) {
          JOptionPane.showMessageDialog(this, "First Name, Last Name, and Date are required fields.");
          return false;
      }
      return true;
  }
  private void submitIncident() {
      if (!validateFormFields()) {
          return;
      }

      try {
          //date should be in dd/MM/yyyy format
          String dateString = incDate.getText();
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          sdf.setLenient(false);

          // Check if the date entered matches the expected format
          Date parsedDate = null;
          try {
              parsedDate = sdf.parse(dateString);
          } catch (Exception e) {
              JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy.");
              return;
          }

          // Convert the Date object to java.sql.Date
          java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

          // check if the record exists
          try (Connection conn = getConnection()) {
              String checkQuery = "SELECT COUNT(*) FROM IncidentDB WHERE incident_firstName = ? " +
                                   "AND incident_lastName = ? AND incident_date = ?";
              try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                  checkStmt.setString(1, incFName.getText());
                  checkStmt.setString(2, incLName.getText());
                  checkStmt.setDate(3, sqlDate);

                  ResultSet rs = checkStmt.executeQuery();
                  rs.next();
                  int count = rs.getInt(1);

                  if (count > 0) {
                      // If the record exists,  UPDATE
                      String updateQuery = "UPDATE IncidentDB SET incident_midName = ?, incident_suffix = ?, " +
                                           "incident_type = ?, incident_description = ?, incident_progress = ? " +
                                           "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                      try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                          updateStmt.setString(1, incMidName.getText());
                          updateStmt.setString(2, incSufName.getText());
                          updateStmt.setString(3, (String) incType.getSelectedItem());
                          updateStmt.setString(4, incDescrip.getText());
                          updateStmt.setString(5, (String) incProg.getSelectedItem());
                          updateStmt.setString(6, incFName.getText());
                          updateStmt.setString(7, incLName.getText());
                          updateStmt.setDate(8, sqlDate);

                          int rowsUpdated = updateStmt.executeUpdate();
                          if (rowsUpdated > 0) {
                              JOptionPane.showMessageDialog(this, "Incident Report Saved Successfully!");
                          } else {
                              JOptionPane.showMessageDialog(this, "No matching record found to update.");
                          }
                      }
                  } else {
                      // If the record doesn't exist,  INSERT
                      String insertQuery = "INSERT INTO IncidentDB (incident_firstName, incident_midName, " +
                                           "incident_lastName, incident_suffix, incident_date, incident_type, " +
                                           "incident_description, incident_progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                      try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                          insertStmt.setString(1, incFName.getText());
                          insertStmt.setString(2, incMidName.getText());
                          insertStmt.setString(3, incLName.getText());
                          insertStmt.setString(4, incSufName.getText());
                          insertStmt.setDate(5, sqlDate); // Use the parsed SQL Date here
                          insertStmt.setString(6, (String) incType.getSelectedItem());
                          insertStmt.setString(7, incDescrip.getText());
                          insertStmt.setString(8, (String) incProg.getSelectedItem());

                          insertStmt.executeUpdate();
                          JOptionPane.showMessageDialog(this, "Incident Report Submitted Successfully!");
                      }
                  }

                  // Clear the form fields
                  clearFormFields();
                  
                  //for a new entry
                  incFName.requestFocus();

              }
          } catch (SQLException e) {
              JOptionPane.showMessageDialog(this, "Error submitting incident report: " + e.getMessage());
          }
      } catch (Exception e) {
          // Log unexpected errors
          JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
      }
  }
  private void clearFormFields() {
      // reset form for a new entry
      incFName.setText("");
      incMidName.setText("");
      incLName.setText("");
      incSufName.setText("");
      incDate.setText("");
      incType.setSelectedIndex(0); 
      incProg.setSelectedIndex(0); 
      incDescrip.setText("Enter a message...");
      incDescrip.setForeground(Color.LIGHT_GRAY);
  }

  public void fillData(String firstName, String middleName, String lastName, String suffix, String date, String progress, String description, String typeOfIncident) {
    // Set the fields in CrimeForm with the passed data
    incFName.setText(firstName);       
    incMidName.setText(middleName);     
    incLName.setText(lastName);        
    incSufName.setText(suffix);         
    incDate.setText(date);             
    incType.setSelectedItem(typeOfIncident); 
    incDescrip.setText(description);   
    incProg.setSelectedItem(progress); 
}
}