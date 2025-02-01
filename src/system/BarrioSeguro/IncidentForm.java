package system.BarrioSeguro; // This line says the code belongs to the "BarrioSeguro" group 

import java.awt.Color; // This is used to add colors
import java.awt.Font; // This is used to add different text styles
import java.awt.Graphics; // This is used to draw things in the window
import java.awt.Graphics2D; // This is used for more advanced drawing tools
import java.awt.Insets; // This is used to set space around the text 
import java.awt.RenderingHints; // This helps make drawn things look better

import java.awt.event.FocusEvent; // This is used to detect when we click or leave a text box
import java.awt.event.FocusListener; // This is used to know when we interact with the text box

import java.sql.Connection; // This is used to connect to the database
import java.sql.PreparedStatement; // This is used to run database commands
import java.sql.ResultSet; // This is used to store data from the database
import java.sql.SQLException; // This is used to handle database errors

import java.text.SimpleDateFormat; // This is used to format dates

import java.util.Date; // This is used to work with dates

import javax.swing.DefaultComboBoxModel; // This helps to manage the dropdown menu items 
import javax.swing.JButton; // This is used to add buttons
import javax.swing.JComboBox; // This is used to add dropdown menus
import javax.swing.JLabel; // This is used to add text labels
import javax.swing.JLayeredPane; // This is used to layer components over each other
import javax.swing.JOptionPane; // This is used to show messages to the user
import javax.swing.JPanel; // This will hold different parts of the layout
import javax.swing.JTextArea; // This adds area where we can write lots of text
import javax.swing.JTextField; // This adds boxes where we can write text 
import javax.swing.SwingConstants; // This is used to align text

import javax.swing.border.EmptyBorder; // This adds space around the text fields

public class IncidentForm extends BaseForm { // This line defines our main Incident form

    // Creating text fields to enter incident details
    private JTextField incidentFirstName; // Field for first name
    private JTextField incidentMidName; // Field for middle name
    private JTextField incidentLastName; // Field for last name
    private JTextField incidentSuffixName; // Field for suffix name
    private JTextField incidentDate; // Field for date
    private JComboBox<String> incidentType; // Dropdown for types of incidents
    private JTextArea incidentDescription; // Area for incident description
    private JComboBox<String> incidentProgress; // Dropdown for incident progress

    // Constructor to set up the form
    public IncidentForm(BarrioSeguro appController) {
        super(appController); // Calling the parent class constructor
        setTitle("BarrioSeguro - Incident Reports"); // Setting the window title
        initialize(); // Calling the method to set up everything
    }

    // Method to set up the form layout and components
    private void initialize() {
        JLayeredPane incidentPane = new JLayeredPane(); // Creating a layer to add components
        setContentPane(incidentPane); // Making incidentPane the main content layer

        addBackgroundImage(incidentPane); // Adding background image to the layer
        addDashboardPanel(incidentPane); // Adding dashboard panel to the layer
        addIncidentPanel(incidentPane); // Adding the panel to enter incident details
    }

    // Method to add the panel where user enters incident details
    @SuppressWarnings("unused")
    private void addIncidentPanel(JLayeredPane incidentPane) {
        JPanel incidentPanel = new JPanel() { // Creating a new panel for the form
            @Override
            protected void paintComponent(Graphics paintGraphics) { // Customizing how the panel looks
                super.paintComponent(paintGraphics); // Calling the default painting method
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics; // Using advanced graphics
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Making it look smooth
                paintGraphicsWith2D.setColor(getBackground()); // Setting background color
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75); // Drawing a rounded rectangle
            }
        };
        incidentPanel.setBounds(480, 185, 895, 644); // Setting the position and size of the panel
        incidentPanel.setBackground(new Color(102, 77, 77, 178)); // Setting background color with transparency
        incidentPanel.setLayout(null); // Using absolute positioning
        incidentPanel.setOpaque(false); // Making the panel background transparent
        incidentPane.add(incidentPanel, JLayeredPane.PALETTE_LAYER); // Adding panel to the main layer
        
        // Setting up the text field for first name
        incidentFirstName = new JTextField(""); // Initial empty state
        incidentFirstName.setToolTipText(""); // No tooltip text
        incidentFirstName.setHorizontalAlignment(SwingConstants.LEFT); // Aligning text to the left
        incidentFirstName.setBackground(new Color(255, 244, 244)); // Background color
        incidentFirstName.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Text font and size
        incidentFirstName.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around text field
        incidentFirstName.setBounds(40, 80, 216, 36); // Position and size of the text field
        incidentPanel.add(incidentFirstName); // Adding text field to the panel
        
        // Setting up the text field for middle name (same steps as first name)
        incidentMidName = new JTextField("");
        incidentMidName.setToolTipText("");
        incidentMidName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentMidName.setBackground(new Color(255, 244, 244));
        incidentMidName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentMidName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentMidName.setBounds(40, 142, 216, 36);
        incidentPanel.add(incidentMidName);
        
        // Setting up the text field for last name (same steps as first name)
        incidentLastName = new JTextField("");
        incidentLastName.setToolTipText("");
        incidentLastName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentLastName.setBackground(new Color(255, 244, 244));
        incidentLastName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentLastName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentLastName.setBounds(40, 204, 216, 36);
        incidentPanel.add(incidentLastName);
        
        // Setting up the text field for suffix name (same steps as first name)
        incidentSuffixName = new JTextField("");
        incidentSuffixName.setToolTipText("");
        incidentSuffixName.setHorizontalAlignment(SwingConstants.LEFT);
        incidentSuffixName.setBackground(new Color(255, 244, 244));
        incidentSuffixName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentSuffixName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentSuffixName.setBounds(40, 266, 216, 36);
        incidentPanel.add(incidentSuffixName);
        
        // Setting up the text field for the date (same steps as first name)
        incidentDate = new JTextField("");
        incidentDate.setToolTipText("");
        incidentDate.setHorizontalAlignment(SwingConstants.LEFT);
        incidentDate.setBackground(new Color(255, 244, 244));
        incidentDate.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incidentDate.setBorder(new EmptyBorder(10, 10, 10, 10));
        incidentDate.setBounds(40, 328, 216, 36);
        incidentPanel.add(incidentDate);
        
        // Setting up the dropdown for incident types
        incidentType = new JComboBox<String>();
        incidentType.setBackground(new Color(255, 244, 244));
        incidentType.setModel(new DefaultComboBoxModel<String>(new String[] {"Assault", "Burglary", "Domestic Violence", "Drug Possession", "Fraud", "Harassment", "Physical Abuse", "Theft", "Traffic Violation", "Vandalism", "Arson", "Robbery", "Homicide", "Kidnapping", "Shoplifting", "Identity Theft", "Embezzlement", "Money Laundering", "Stalking", "Sexual Assault", "Child Abuse", "Human Trafficking", "Battery", "Bribery", "Extortion", "Counterfeiting", "Piracy", "Public Intoxication", "Rape", "Prostitution", "Weapons Possession", "Animal Cruelty", "Blackmail", "Tax Evasion", "Illegal Gambling", "Wire Fraud", "Trespassing", "Insurance Fraud", "Unlawful Detention", "Obstruction of Justice", "Racketeering", "Manslaughter", "Money Counterfeiting", "Corruption", "Illegal Search and Seizure", "Forgery", "Drug Trafficking", "Terrorism", "Public Disorder", "Bribing a Witness", "Reckless Driving", "Destruction of Property", "Coercion"})); // Adding incident types
        incidentType.setBounds(40, 390, 216, 36); // Position and size of the dropdown
        incidentPanel.add(incidentType);
        
        // Setting up the dropdown for incident progress
        incidentProgress = new JComboBox<String>();
        incidentProgress.setModel(new DefaultComboBoxModel<String>(new String[] {"Under Investigation", "Ongoing", "Resolved", "Closed"}));
        incidentProgress.setBounds(40, 452, 216, 36); // Position and size of the dropdown
        incidentPanel.add(incidentProgress);
        
        // Setting up the text area for incident description
        incidentDescription = createRoundedTextArea("Enter a message...", 583, 423); // Custom method to create a text area
        incidentDescription.setForeground(Color.BLACK); // Set initial color to black

        incidentDescription.setWrapStyleWord(true); // Words won't break
        incidentDescription.setBounds(273, 65, 583, 423); // Position and size of the text area
        incidentDescription.setLineWrap(true); // Text wraps to the next line

        incidentDescription.setMargin(new Insets(20, 20, 20, 20)); // Padding around text area
        incidentDescription.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForIncidentDesc) { // When the text area is clicked
                if (incidentDescription.getText().equals("Enter a message...")) {
                    incidentDescription.setText(""); // Clear the default text
                    incidentDescription.setForeground(Color.BLACK); // Set text color to black
                }
            }

            @Override
            public void focusLost(FocusEvent eventForIncidentDesc) { // When the text area loses focus
                if (incidentDescription.getText().isEmpty()) {
                    incidentDescription.setText("Enter a message..."); // Reset to default text
                    incidentDescription.setForeground(Color.LIGHT_GRAY); // Set text color to gray
                }
            }
        });
        incidentPanel.add(incidentDescription); // Add text area to the panel

        // Setting up labels for the text fields
        JLabel lblNewLabel = new JLabel("Enter First Name");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblNewLabel.setBounds(40, 65, 118, 19);
        incidentPanel.add(lblNewLabel);
        
        // Setting up label for middle name (same steps as first name label)
        JLabel lblEnterMiddleName = new JLabel("Enter Middle Name (empty if none)");
        lblEnterMiddleName.setForeground(new Color(255, 255, 255));
        lblEnterMiddleName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterMiddleName.setBounds(40, 127, 216, 18);
        incidentPanel.add(lblEnterMiddleName);
        
        // Setting up label for last name (same steps as first name label)
        JLabel lblEnterLastName = new JLabel("Enter Last Name");
        lblEnterLastName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterLastName.setForeground(new Color(255, 255, 255));
        lblEnterLastName.setBounds(40, 189, 184, 18);
        incidentPanel.add(lblEnterLastName);
        
        // Setting up label for suffix name (same steps as first name label)
        JLabel lblEnterSuffixempty = new JLabel("Enter Suffix (empty if none)");
        lblEnterSuffixempty.setForeground(new Color(255, 255, 255));
        lblEnterSuffixempty.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterSuffixempty.setBounds(40, 251, 184, 18);
        incidentPanel.add(lblEnterSuffixempty);
        
        // Setting up label for the date (same steps as first name label)
        JLabel lblEnterDate = new JLabel("Enter Date: DD/MM/YYYY");
        lblEnterDate.setForeground(new Color(255, 255, 255));
        lblEnterDate.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterDate.setBounds(40, 313, 184, 18);
        incidentPanel.add(lblEnterDate);
        
        // Setting up label for incident type (same steps as first name label)
        JLabel lblChooseAType = new JLabel("Choose a type of Incident");
        lblChooseAType.setForeground(new Color(255, 255, 255));
        lblChooseAType.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblChooseAType.setBounds(40, 375, 184, 21);
        incidentPanel.add(lblChooseAType);
        
        // Setting up label for incident progress (same steps as first name label)
        JLabel lblIncidentProgress = new JLabel("Incident Progress");
        lblIncidentProgress.setForeground(new Color(255, 255, 255));
        lblIncidentProgress.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblIncidentProgress.setBounds(40, 437, 184, 18);
        incidentPanel.add(lblIncidentProgress);
        
        // Setting up submit button
        JButton submitbtn = new JButton("Submit");
        styleRoundedButton(submitbtn); // Custom method to style the button
        submitbtn.setBounds(378, 529, 150, 55); // Position and size of the button
        submitbtn.addActionListener(eventForSubmitBtn -> { // When the button is clicked
            if (validateIncidentDescription()) { // Check if description is valid
                submitIncident(); // Submit the incident
            }
        });
        incidentPanel.add(submitbtn); // Add the button to the panel
    }
    
    // Method to validate that the description is filled in
    private boolean validateIncidentDescription() {
        if (incidentDescription.getText().equals("Enter a message...") || incidentDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Incident description is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Method to validate important fields are filled in
    private boolean validateFormFields() {
        if (incidentFirstName.getText().isEmpty() || incidentLastName.getText().isEmpty() || incidentDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name, Last Name, and Date are required fields.");
            return false;
        }
        return true;
    }

    // Method for submitting the incident form to the database
    private void submitIncident() {
        if (!validateFormFields()) { // Check if required fields are filled
            return;
        }

        try {
            String dateString = incidentDate.getText(); // Get date from text field
            SimpleDateFormat formatDateString = new SimpleDateFormat("dd/MM/yyyy"); // Date format we want
            formatDateString.setLenient(false); // Exact format is expected

            Date parsedDate = null;
            try {
                parsedDate = formatDateString.parse(dateString); // Try to convert text to date
            } catch (Exception handleParsedDateException) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy.");
                return;
            }

            java.sql.Date convertedSQLdate = new java.sql.Date(parsedDate.getTime()); // Convert to SQL date

            try (Connection connectSubmitIncident = getConnection()) { // Establish database connection

                String checkQuery = "SELECT COUNT(*) FROM IncidentDB WHERE incident_firstName = ? " +
                                    "AND incident_lastName = ? AND incident_date = ?"; // SQL query
                try (PreparedStatement checkStmt = connectSubmitIncident.prepareStatement(checkQuery)) {
                    // Fill the SQL query with form data
                    checkStmt.setString(1, incidentFirstName.getText());
                    checkStmt.setString(2, incidentLastName.getText());
                    checkStmt.setDate(3, convertedSQLdate);

                    // Execute the query
                    ResultSet resultSubmitIncident = checkStmt.executeQuery();
                    resultSubmitIncident.next();
                    int count = resultSubmitIncident.getInt(1);

                    if (count > 0) {
                        // Update query if entry exists
                        String updateQuery = "UPDATE IncidentDB SET incident_midName = ?, incident_suffix = ?, " + 
                                            "incident_type = ?, incident_description = ?, incident_progress = ? " +
                                            "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                        try (PreparedStatement updateStmt = connectSubmitIncident.prepareStatement(updateQuery)) {
                            // Fill the update query with form data
                            updateStmt.setString(1, incidentMidName.getText());
                            updateStmt.setString(2, incidentSuffixName.getText());
                            updateStmt.setString(3, (String) incidentType.getSelectedItem());
                            updateStmt.setString(4, incidentDescription.getText());
                            updateStmt.setString(5, (String) incidentProgress.getSelectedItem());
                            updateStmt.setString(6, incidentFirstName.getText());
                            updateStmt.setString(7, incidentLastName.getText());
                            updateStmt.setDate(8, convertedSQLdate);

                            int rowsUpdated = updateStmt.executeUpdate(); // Execute update
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(this, "Incident Report Saved Successfully!");
                            } else {
                                JOptionPane.showMessageDialog(this, "No matching record found to update.");
                            }
                        }
                    } else {
                        // Insert query if no matching entry
                        String insertQuery = "INSERT INTO IncidentDB (incident_firstName, incident_midName, " +
                                            "incident_lastName, incident_suffix, incident_date, incident_type, " +
                                            "incident_description, incident_progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement insertStmt = connectSubmitIncident.prepareStatement(insertQuery)) {
                            // Fill the insert query with form data
                            insertStmt.setString(1, incidentFirstName.getText());
                            insertStmt.setString(2, incidentMidName.getText());
                            insertStmt.setString(3, incidentLastName.getText());
                            insertStmt.setString(4, incidentSuffixName.getText());
                            insertStmt.setDate(5, convertedSQLdate);
                            insertStmt.setString(6, (String) incidentType.getSelectedItem());
                            insertStmt.setString(7, incidentDescription.getText());
                            insertStmt.setString(8, (String) incidentProgress.getSelectedItem());

                            insertStmt.executeUpdate(); // Execute insert
                            JOptionPane.showMessageDialog(this, "Incident Report Submitted Successfully!");
                        }
                    }

                    clearFormFields(); // Clear form fields after submission
                    
                    incidentFirstName.requestFocus(); // Set focus back to the first name field
                }
            } catch (SQLException handleDatabaseException) {
                JOptionPane.showMessageDialog(this, "Error submitting incident report: " + handleDatabaseException.getMessage());
            }
        } catch (Exception handleSubmitException) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + handleSubmitException.getMessage());
        }
    }

    // Clear function after successfully inputting incident
    private void clearFormFields() { 
        incidentFirstName.setText("");
        incidentMidName.setText("");
        incidentLastName.setText("");
        incidentSuffixName.setText("");
        incidentDate.setText("");
        incidentType.setSelectedIndex(0); // Reset incident type
        incidentProgress.setSelectedIndex(0); // Reset incident progress
        incidentDescription.setText("Enter a message...");
        incidentDescription.setForeground(Color.LIGHT_GRAY); // Reset description color
    }

    // Function for to be used in summaryForm (Fill form with given data)
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