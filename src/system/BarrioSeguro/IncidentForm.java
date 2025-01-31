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

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;

public class IncidentForm extends BaseForm {

    private JTextField incidentFirstName;
    private JTextField incidentMidName;
    private JTextField incidentLastName;
    private JTextField incidentSuffixName;
    private JTextField incidentDate;
    private JComboBox<String> incidentType;
    private JTextArea incidentDescription;
    private JComboBox<String> incidentProgress;

    public IncidentForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Incident Reports");
        initialize();
    }

    private void initialize() {
        JLayeredPane incidentPane = new JLayeredPane();
        setContentPane(incidentPane);

        addBackgroundImage(incidentPane);
        addDashboardPanel(incidentPane);
        addIncidentPanel(incidentPane);
    }

    @SuppressWarnings("unused")
    private void addIncidentPanel(JLayeredPane incidentPane) {
        JPanel incidentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        incidentPanel.setBounds(480, 185, 895, 644);
        incidentPanel.setBackground(new Color(102, 77, 77, 178));
        incidentPanel.setLayout(null);
        incidentPanel.setOpaque(false);
        incidentPane.add(incidentPanel, JLayeredPane.PALETTE_LAYER);
        
        incidentFirstName = new JTextField("");
        incidentFirstName.setToolTipText("");
        incidentFirstName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentFirstName.setBackground(new Color(255, 244, 244));
        incidentFirstName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentFirstName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentFirstName.setBounds(40, 80, 216, 36);
        
        incidentPanel.add(incidentFirstName);
        
        incidentMidName = new JTextField("");
        incidentMidName.setToolTipText("");
        incidentMidName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentMidName.setBackground(new Color(255, 244, 244));
        incidentMidName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentMidName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentMidName.setBounds(40, 142, 216, 36);
        incidentPanel.add(incidentMidName);
        
        incidentLastName = new JTextField("");
        incidentLastName.setToolTipText("");
        incidentLastName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentLastName.setBackground(new Color(255, 244, 244));
        incidentLastName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentLastName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentLastName.setBounds(40, 204, 216, 36);
        incidentPanel.add(incidentLastName);
        
        incidentSuffixName = new JTextField("");
        incidentSuffixName.setToolTipText("");
        incidentSuffixName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentSuffixName.setBackground(new Color(255, 244, 244));
        incidentSuffixName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentSuffixName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentSuffixName.setBounds(40, 266, 216, 36);
        incidentPanel.add(incidentSuffixName);
        
        incidentDate = new JTextField("");
        incidentDate.setToolTipText("");
        incidentDate.setHorizontalAlignment(SwingConstants.LEFT);
        incidentDate.setBackground(new Color(255, 244, 244));
        incidentDate.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentDate.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentDate.setBounds(40, 328, 216, 36);
        incidentPanel.add(incidentDate);
        
        incidentType = new JComboBox<String>();
        incidentType.setBackground(new Color(255, 244, 244));
        incidentType.setModel(new DefaultComboBoxModel<String>(new String[] {"Assault", "Burglary", "Domestic Violence", "Drug Possession", "Fraud", "Harassment", "Physical Abuse", "Theft", "Traffic Violation", "Vandalism", "Arson", "Robbery", "Homicide", "Kidnapping", "Shoplifting", "Identity Theft", "Embezzlement", "Money Laundering", "Stalking", "Sexual Assault", "Child Abuse", "Human Trafficking", "Battery", "Bribery", "Extortion", "Counterfeiting", "Piracy", "Public Intoxication", "Rape", "Prostitution", "Weapons Possession", "Animal Cruelty", "Blackmail", "Tax Evasion", "Illegal Gambling", "Wire Fraud", "Trespassing", "Insurance Fraud", "Unlawful Detention", "Obstruction of Justice", "Racketeering", "Manslaughter", "Money Counterfeiting", "Corruption", "Illegal Search and Seizure", "Forgery", "Drug Trafficking", "Terrorism", "Public Disorder", "Bribing a Witness", "Reckless Driving", "Destruction of Property", "Coercion"}));
        incidentType.setBounds(40, 390, 216, 36);
        incidentPanel.add(incidentType);
        
        incidentProgress = new JComboBox<String>();
        incidentProgress.setBackground(new Color(255, 244, 244));
        incidentProgress.setModel(new DefaultComboBoxModel<String>(new String[] {"Under Investigation", "Ongoing", "Resolved", "Closed"}));
        incidentProgress.setBounds(40, 452, 216, 36);
        incidentPanel.add(incidentProgress);
        
        incidentDescription = createRoundedTextArea("Enter a message...", 583, 423);
        incidentDescription.setForeground(Color.BLACK);
        incidentDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
        incidentDescription.setWrapStyleWord(true);
        incidentDescription.setBounds(273, 65, 583, 423);
        incidentDescription.setLineWrap(true);
        
        incidentDescription.setMargin(new Insets(20, 20, 20, 20));
        incidentDescription.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForIncidentDesc) {
                if (incidentDescription.getText().equals("Enter a message...")) {
                    incidentDescription.setText(""); 
                    incidentDescription.setForeground(Color.BLACK); 
                }
            }

            @Override
            public void focusLost(FocusEvent eventForIncidentDesc) {
                if (incidentDescription.getText().isEmpty()) {
                    incidentDescription.setText("Enter a message..."); 
                    incidentDescription.setForeground(Color.LIGHT_GRAY); 
                }
            }
        });
        incidentPanel.add(incidentDescription);
        
        JLabel lblNewLabel = new JLabel("Enter First Name");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblNewLabel.setBounds(40, 65, 118, 19);
        incidentPanel.add(lblNewLabel);
        
        JLabel lblEnterMiddleName = new JLabel("Enter Middle Name (empty if none)");
        lblEnterMiddleName.setForeground(new Color(255, 255, 255));
        lblEnterMiddleName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterMiddleName.setBounds(40, 127, 216, 18);
        incidentPanel.add(lblEnterMiddleName);
        
        JLabel lblEnterLastName = new JLabel("Enter Last Name");
        lblEnterLastName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterLastName.setForeground(new Color(255, 255, 255));
        lblEnterLastName.setBounds(40, 189, 184, 18);
        incidentPanel.add(lblEnterLastName);
        
        JLabel lblEnterSuffixempty = new JLabel("Enter Suffix (empty if none)");
        lblEnterSuffixempty.setForeground(new Color(255, 255, 255));
        lblEnterSuffixempty.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterSuffixempty.setBounds(40, 251, 184, 18);
        incidentPanel.add(lblEnterSuffixempty);
        
        JLabel lblEnterDate = new JLabel("Enter Date: DD/MM/YYYY");
        lblEnterDate.setForeground(new Color(255, 255, 255));
        lblEnterDate.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterDate.setBounds(40, 313, 184, 18);
        incidentPanel.add(lblEnterDate);
        
        JLabel lblChooseAType = new JLabel("Choose a type of Incident");
        lblChooseAType.setForeground(new Color(255, 255, 255));
        lblChooseAType.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblChooseAType.setBounds(40, 375, 184, 21);
        incidentPanel.add(lblChooseAType);
        
        JLabel lblIncidentProgress = new JLabel("Incident Progress");
        lblIncidentProgress.setForeground(new Color(255, 255, 255));
        lblIncidentProgress.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblIncidentProgress.setBounds(40, 437, 184, 18);
        incidentPanel.add(lblIncidentProgress);
        
        JButton submitbtn = new JButton("Submit");
        styleRoundedButton(submitbtn);
        submitbtn.setBounds(363, 529, 150, 55);
        submitbtn.addActionListener(eventForSubmitBtn -> submitIncident());
        incidentPanel.add(submitbtn);
    }
    
    private boolean validateFormFields() {
        if (incidentFirstName.getText().isEmpty() || incidentLastName.getText().isEmpty() || incidentDate.getText().isEmpty()) {
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
            String dateString = incidentDate.getText();
            SimpleDateFormat formatDateString = new SimpleDateFormat("dd/MM/yyyy");
            formatDateString.setLenient(false);

            Date parsedDate = null;
            try {
                parsedDate = formatDateString.parse(dateString);
            } catch (Exception handleParsedDateException) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy.");
                return;
            }

            java.sql.Date convertedSQLdate = new java.sql.Date(parsedDate.getTime());

            try (Connection connectSubmitIncident = getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM IncidentDB WHERE incident_firstName = ? " +
                                    "AND incident_lastName = ? AND incident_date = ?";
                try (PreparedStatement checkStmt = connectSubmitIncident.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, incidentFirstName.getText());
                    checkStmt.setString(2, incidentLastName.getText());
                    checkStmt.setDate(3, convertedSQLdate);

                    ResultSet resultSubmitIncident = checkStmt.executeQuery();
                    resultSubmitIncident.next();
                    int count = resultSubmitIncident.getInt(1);

                    if (count > 0) {
                        String updateQuery = "UPDATE IncidentDB SET incident_midName = ?, incident_suffix = ?, " +
                                            "incident_type = ?, incident_description = ?, incident_progress = ? " +
                                            "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                        try (PreparedStatement updateStmt = connectSubmitIncident.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, incidentMidName.getText());
                            updateStmt.setString(2, incidentSuffixName.getText());
                            updateStmt.setString(3, (String) incidentType.getSelectedItem());
                            updateStmt.setString(4, incidentDescription.getText());
                            updateStmt.setString(5, (String) incidentProgress.getSelectedItem());
                            updateStmt.setString(6, incidentFirstName.getText());
                            updateStmt.setString(7, incidentLastName.getText());
                            updateStmt.setDate(8, convertedSQLdate);

                            int rowsUpdated = updateStmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(this, "Incident Report Saved Successfully!");
                            } else {
                                JOptionPane.showMessageDialog(this, "No matching record found to update.");
                            }
                        }
                    } else {
                        String insertQuery = "INSERT INTO IncidentDB (incident_firstName, incident_midName, " +
                                            "incident_lastName, incident_suffix, incident_date, incident_type, " +
                                            "incident_description, incident_progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement insertStmt = connectSubmitIncident.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, incidentFirstName.getText());
                            insertStmt.setString(2, incidentMidName.getText());
                            insertStmt.setString(3, incidentLastName.getText());
                            insertStmt.setString(4, incidentSuffixName.getText());
                            insertStmt.setDate(5, convertedSQLdate);
                            insertStmt.setString(6, (String) incidentType.getSelectedItem());
                            insertStmt.setString(7, incidentDescription.getText());
                            insertStmt.setString(8, (String) incidentProgress.getSelectedItem());

                            insertStmt.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Incident Report Submitted Successfully!");
                        }
                    }

                    clearFormFields();
                    
                    incidentFirstName.requestFocus();
                }
            } catch (SQLException handleDatabaseException) {
                JOptionPane.showMessageDialog(this, "Error submitting incident report: " + handleDatabaseException.getMessage());
            }
        } catch (Exception handleSubmitException) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + handleSubmitException.getMessage());
        }
    }

    private void clearFormFields() {
        incidentFirstName.setText("");
        incidentMidName.setText("");
        incidentLastName.setText("");
        incidentSuffixName.setText("");
        incidentDate.setText("");
        incidentType.setSelectedIndex(0); 
        incidentProgress.setSelectedIndex(0); 
        incidentDescription.setText("Enter a message...");
        incidentDescription.setForeground(Color.LIGHT_GRAY);
    }

    public void fillData(String firstName, String middleName, String lastName, String suffix, String date, String progress, String description, String typeOfIncident) {
        incidentFirstName.setText(firstName);       
        incidentMidName.setText(middleName);     
        incidentLastName.setText(lastName);        
        incidentSuffixName.setText(suffix);         
        incidentDate.setText(date);             
        incidentType.setSelectedItem(typeOfIncident); 
        incidentDescription.setText(description);   
        incidentProgress.setSelectedItem(progress); 
    }
}