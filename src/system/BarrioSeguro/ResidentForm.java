// This line says our file belongs to package "system.BarrioSeguro"
// A package is like a folder that keeps similar classes together
package system.BarrioSeguro;

// This line says our file belongs to package "system.BarrioSeguro"
// A package is like a folder that keeps similar classes together
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

// Our class "ResidentForm" extends "BaseForm," so it inherits features like window setup
public class ResidentForm extends BaseForm {

    // "residentTable" will show our rows of resident data
    private JTable residentTable;
    // "searchTextField" will let us type text for searching the table
    private JTextField searchTextField;

    // This constructor runs when we create "ResidentForm"
    // It sets the title and calls methods to build the screen and load data
    public ResidentForm(BarrioSeguro appController) {
        // Tells the parent class (BaseForm) to set basic window info
        super(appController);
        // Sets the window’s title bar text
        setTitle("BarrioSeguro - Resident Database");
        // Calls a private method to set up the layout and panels
        initialize();
        // Loads existing resident data from the database into the table
        loadResidentData();
    }

    // Private method "initialize" builds the main layout inside the form
    private void initialize() {
        // "JLayeredPane" allows stacking of components (like background, panels)
        JLayeredPane residentPane = new JLayeredPane();
        // Attach this pane as the main content of our window
        setContentPane(residentPane);

        // Add a background image from the base class
        addBackgroundImage(residentPane);
        // Add a dashboard panel with home, announcements, etc.
        addDashboardPanel(residentPane);
        // Add a panel that displays and manages resident information
        addResidentPanel(residentPane);
    }
    
    // "addResidentToDatabase" inserts a new resident into our SQL database
    // It uses eight pieces of info: names, address, birth date, contact info, etc.
    private void addResidentToDatabase(String firstName, String middleName, String lastName, String suffix, String address, String dateOfBirth, String contact, String email) {
        // Here is the SQL command that inserts new rows into "ResidentDB"
        // We provide values for each column: firstName, midName, lastName, suffix, address, DoB, contactNo, email
        String query = "INSERT INTO ResidentDB (resident_firstName, resident_midName, resident_lastName, resident_suffix, resident_address, resident_DoB, resident_contactNo, resident_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (
            // "getConnection" is from our "BaseForm" to open the database
            Connection connectAddResident = getConnection();
            // "PreparedStatement" helps us safely place variables into the query
            PreparedStatement prepareAddResident = connectAddResident.prepareStatement(query)
        ) {
            // Convert the String date into a proper SQL date using our helper function
            java.sql.Date convertSQLdate = convertToDate(dateOfBirth); // convert date so that format is unison mm/dd/yyyy
            
            // Insert data into the 1st question mark for firstName, 2nd for middleName, etc.
            prepareAddResident.setString(1, firstName);
            // If "middleName" is empty, we store NULL in the database
            prepareAddResident.setString(2, middleName.isEmpty() ? null : middleName);
            prepareAddResident.setString(3, lastName);
            prepareAddResident.setString(4, suffix.isEmpty() ? null : suffix);
            prepareAddResident.setString(5, address);
            prepareAddResident.setDate(6, convertSQLdate);
            prepareAddResident.setString(7, contact);
            prepareAddResident.setString(8, email);
            
            // Execute the query to insert a row, and store how many rows were added
            int rowsInserted = prepareAddResident.executeUpdate();
            // If at least one row is added, we show a success message
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(ResidentForm.this, "Resident added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the table to show new entries
                loadResidentData();
            }
        } catch (ParseException handleParseAddResident) {
            // If the date is in the wrong format, show an error
            JOptionPane.showMessageDialog(ResidentForm.this, "Invalid Date of Birth format. Please use dd/MM/yyyy.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException handleDatabaseAddResident) {
            // If the database is unreachable or another SQL error occurs
            handleDatabaseAddResident.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error adding resident: " + handleDatabaseAddResident.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // This helper method "convertToDate" turns "dd/MM/yyyy" text into a java.sql.Date
    private java.sql.Date convertToDate(String dateOfBirth) throws ParseException {
        // "SimpleDateFormat" can parse a date from a string in "dd/MM/yyyy"
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        // Parse the string into a normal "Date"
        java.util.Date parsedDate = inputFormat.parse(dateOfBirth);
        // Convert it into a SQL date, which the database accepts
        return new java.sql.Date(parsedDate.getTime());
    }

    // "addResidentPanel" builds the section that displays and manages our resident table
    private void addResidentPanel(JLayeredPane residentPane) {
        // Make a new panel with a custom paint method for round corners or shapes
        JPanel addingResidentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                // Call the default paint method first
                super.paintComponent(paintGraphics);
                // Then we cast it into 2D for smoother shapes
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fill our panel background with the chosen color
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        // Place it at x=480, y=185, width=895, height=722
        addingResidentPanel.setBounds(480, 185, 895, 722);
        // The color is a semi-transparent shade (R=102, G=77, B=77, alpha=178)
        addingResidentPanel.setBackground(new Color(102, 77, 77, 178)); 
        // We’ll arrange items manually
        addingResidentPanel.setLayout(null);
        // Make the panel see-through
        addingResidentPanel.setOpaque(false);

        // Add this panel onto the "residentPane"
        residentPane.add(addingResidentPanel, JLayeredPane.PALETTE_LAYER);
        
        // Create our table that shows the resident data
        residentTable = new JTable();
        // A scrollable container that lets us scroll through the table if it’s long
        JScrollPane scrollResidentTable = new JScrollPane(residentTable);
        // Position the scroll area on the panel at x=22, y=93, width=848, height=483
        scrollResidentTable.setBounds(22, 93, 848, 483);
        addingResidentPanel.add(scrollResidentTable);;

        // Build a text field to handle searching
        searchTextField = createRoundedTextField("Search", 25);
        // No tooltip text
        searchTextField.setToolTipText("");
        // The text is aligned to the left
        searchTextField.setHorizontalAlignment(SwingConstants.LEFT);
        // Light gray text color for placeholder
        searchTextField.setForeground(Color.LIGHT_GRAY);
        // Background is white
        searchTextField.setBackground(new Color(254, 254, 254));
        // Font is plain style, size 12
        searchTextField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        // Empty border to add a little bit of breathing space around text
        searchTextField.setBorder(new EmptyBorder(10, 10, 10, 10));
        // Position of the search bar on the panel
        searchTextField.setBounds(22, 45, 216, 37);

        // When the user clicks inside the box, if the placeholder is "Search," we remove it
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForSearchField) {
                if (searchTextField.getText().equals("Search")) {
                    searchTextField.setText("");
                    searchTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent eventForSearchField) {
                // If the user leaves it empty, put the placeholder back
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Search");
                    searchTextField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        // This "KeyListener" triggers a filter function whenever the user types
        searchTextField.addKeyListener(new KeyAdapter() { // search function
            @Override
            public void keyReleased(KeyEvent eventKeyForSearchField) {
                // We convert everything to lowercase so searching is easier
                String searchQuery = searchTextField.getText().trim().toLowerCase();
                // Filter the table results based on this text
                filterTable(searchQuery);
            }
        });

        // Add the search field to the panel
        addingResidentPanel.add(searchTextField);

        // Make an "Add" button for adding a new resident
        JButton btnAdd = new JButton("Add");
        styleRoundedButton(btnAdd);
        // If they press "Add," we open a small form that collects the new resident’s info
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForAddBtn) {
                // "addResidentInfoPanel" is a small grid layout to hold text fields
                JPanel addResidentInfoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                // We label and then create a text field for each piece of info
                addResidentInfoPanel.add(new JLabel("First Name:"));
                JTextField firstNameField = new JTextField();
                addResidentInfoPanel.add(firstNameField);

                addResidentInfoPanel.add(new JLabel("Middle Name (Optional):"));
                JTextField middleNameField = new JTextField();
                addResidentInfoPanel.add(middleNameField);

                addResidentInfoPanel.add(new JLabel("Last Name:"));
                JTextField lastNameField = new JTextField();
                addResidentInfoPanel.add(lastNameField);

                addResidentInfoPanel.add(new JLabel("Suffix (Optional):"));
                JTextField suffixField = new JTextField();
                addResidentInfoPanel.add(suffixField);

                addResidentInfoPanel.add(new JLabel("Address:"));
                JTextField addressField = new JTextField();
                addResidentInfoPanel.add(addressField);

                addResidentInfoPanel.add(new JLabel("Date of Birth (dd/mm/yyyy):"));
                JTextField dobField = new JTextField();
                addResidentInfoPanel.add(dobField);

                addResidentInfoPanel.add(new JLabel("Contact Number (11 digits):"));
                JTextField contactField = new JTextField();
                addResidentInfoPanel.add(contactField);

                addResidentInfoPanel.add(new JLabel("Email:"));
                JTextField emailField = new JTextField();
                addResidentInfoPanel.add(emailField);

                // We show a simple dialog with “OK” and “Cancel”
                int result = JOptionPane.showConfirmDialog(null, addResidentInfoPanel, "Add Resident",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If the user clicks OK, we collect the text field values
                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameField.getText().trim();
                    String middleName = middleNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String suffix = suffixField.getText().trim();
                    String address = addressField.getText().trim();
                    String dateOfBirth = dobField.getText().trim();
                    String contact = contactField.getText().trim();
                    String email = emailField.getText().trim();

                    // Check if some required fields are empty
                    if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty() || contact.isEmpty() || email.isEmpty()) {
                        // Show a warning if needed
                        JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Contact must be exactly 11 digits, or it’s invalid
                    if (!contact.matches("\\d{11}")) {
                        JOptionPane.showMessageDialog(null, "Contact number must be 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Date must follow dd/mm/yyyy
                    if (!dateOfBirth.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        JOptionPane.showMessageDialog(null, "Date of Birth must be in the format dd/mm/yyyy.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Everything looks okay, so attempt to store it in the database
                    addResidentToDatabase(firstName, middleName, lastName, suffix, address, dateOfBirth, contact, email);
                }
            }
        });
        // Position and add the "Add" button
        btnAdd.setBounds(68, 612, 147, 64);
        addingResidentPanel.add(btnAdd);

        // A button to update an existing record
        JButton btnUpdate = new JButton("Update");
        styleRoundedButton(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForUpdateBtn) {
                int selectedRow = residentTable.getSelectedRow();

                // We check if a row is selected; if not, alert the user
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a resident to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // We gather data from the selected row
                int residentId = (Integer) residentTable.getValueAt(selectedRow, 0);
                String firstName = (String) residentTable.getValueAt(selectedRow, 1);
                String middleName = (String) residentTable.getValueAt(selectedRow, 2);
                String lastName = (String) residentTable.getValueAt(selectedRow, 3);
                String suffix = (String) residentTable.getValueAt(selectedRow, 4);
                String address = (String) residentTable.getValueAt(selectedRow, 5);
                String dateOfBirth = residentTable.getValueAt(selectedRow, 6).toString();
                String contact = (String) residentTable.getValueAt(selectedRow, 7);
                String email = (String) residentTable.getValueAt(selectedRow, 8);

                // We build an "updateResidentInfoPanel" to let us edit existing data
                JPanel updateResidentInfoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                JTextField firstNameField = new JTextField(firstName);
                JTextField middleNameField = new JTextField(middleName);
                JTextField lastNameField = new JTextField(lastName);
                JTextField suffixField = new JTextField(suffix);
                JTextField addressField = new JTextField(address);
                JTextField dobField = new JTextField(dateOfBirth);
                JTextField contactField = new JTextField(contact);
                JTextField emailField = new JTextField(email);

                // Add labels and text fields
                updateResidentInfoPanel.add(new JLabel("First Name:"));
                updateResidentInfoPanel.add(firstNameField);
                updateResidentInfoPanel.add(new JLabel("Middle Name (Optional):"));
                updateResidentInfoPanel.add(middleNameField);
                updateResidentInfoPanel.add(new JLabel("Last Name:"));
                updateResidentInfoPanel.add(lastNameField);
                updateResidentInfoPanel.add(new JLabel("Suffix (Optional):"));
                updateResidentInfoPanel.add(suffixField);
                updateResidentInfoPanel.add(new JLabel("Address:"));
                updateResidentInfoPanel.add(addressField);
                updateResidentInfoPanel.add(new JLabel("Date of Birth (dd/MM/yyyy):"));
                updateResidentInfoPanel.add(dobField);
                updateResidentInfoPanel.add(new JLabel("Contact Number (11 digits):"));
                updateResidentInfoPanel.add(contactField);
                updateResidentInfoPanel.add(new JLabel("Email:"));
                updateResidentInfoPanel.add(emailField);

                // Show a dialog with the new panel
                int result = JOptionPane.showConfirmDialog(null, updateResidentInfoPanel, "Update Resident", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If the user chooses OK, we collect the updated text
                if (result == JOptionPane.OK_OPTION) {
                    String updatedFirstName = firstNameField.getText().trim();
                    String updatedMiddleName = middleNameField.getText().trim();
                    String updatedLastName = lastNameField.getText().trim();
                    String updatedSuffix = suffixField.getText().trim();
                    String updatedAddress = addressField.getText().trim();
                    String updatedDob = dobField.getText().trim();
                    String updatedContact = contactField.getText().trim();
                    String updatedEmail = emailField.getText().trim();

                    // Try to update the database, or show an error if something goes wrong
                    try {
                        updateResidentInDatabase(residentId, updatedFirstName, updatedMiddleName, updatedLastName, updatedSuffix, updatedAddress, updatedDob, updatedContact, updatedEmail);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error updating resident: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Position and add the "Update" button
        btnUpdate.setBounds(268, 612, 147, 62);
        addingResidentPanel.add(btnUpdate);

        // A button to remove a resident record
        JButton btnDel = new JButton("Delete");
        styleRoundedButton(btnDel);
        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForDeleteBtn) {
                int selectedRow = residentTable.getSelectedRow();

                // Check if one row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a resident to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Get the ID for that row
                int residentId = (int) residentTable.getValueAt(selectedRow, 0);

                // Ask the user if they want to delete it for sure
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this resident?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Actually delete the selected row from the database
                    deleteResidentFromDatabase(residentId);
                }
            }
        });
        // Position and add the "Delete" button
        btnDel.setBounds(468, 612, 147, 62);
        addingResidentPanel.add(btnDel);

        // A button to print ID of a resident record
        JButton btnPrintID = new JButton("Print ID");
        styleRoundedButton(btnPrintID);
        btnPrintID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForPrintBtn) {
                int selectedRow = residentTable.getSelectedRow();

                // Check if one row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a resident to print.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Show the yes/no confirmation dialog for printing
                int printConfirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to print this ID?", "Confirm Print", JOptionPane.YES_NO_OPTION);
                
                // Proceed based on the user's response
                if (printConfirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Printed!", "Print Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Print action canceled.", "Print Cancellation", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        // Position and add the "Print ID" button
        btnPrintID.setBounds(668, 612, 147, 62);
        addingResidentPanel.add(btnPrintID);
    }

    // "filterTable" uses a TableRowSorter to show only rows matching user’s search text
    private void filterTable(String searchQuery) {
        DefaultTableModel filterModel = (DefaultTableModel) residentTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(filterModel);
        residentTable.setRowSorter(sorter);

        // If the user typed something, create a filter for that searchQuery
        if (searchQuery.isEmpty() || searchQuery.equalsIgnoreCase("search")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery));
        }
    }

    // "deleteResidentFromDatabase" removes a record by using the ID
    private void deleteResidentFromDatabase(int residentId) {
         // Our DELETE query references "ResidentDB" by "resident_id"
        String query = "DELETE FROM ResidentDB WHERE resident_id = ?";

        try (
                Connection connectDeleteResident = getConnection();
                PreparedStatement prepareDeleteResident = connectDeleteResident.prepareStatement(query)
            ) {
            // Put the correct resident id in the DELETE command
            prepareDeleteResident.setInt(1, residentId);

            // Actually run the command
            int rowsDeleted = prepareDeleteResident.executeUpdate();
            // If at least one row is removed, we show success
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(ResidentForm.this, "Resident deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadResidentData();
            } else {
                JOptionPane.showMessageDialog(ResidentForm.this, "No resident found to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException handleDatabaseDeleteResident) {
            // If something fails, print error details and show a message
            handleDatabaseDeleteResident.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error deleting resident: " + handleDatabaseDeleteResident.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // This function updates an existing record by changing its data in the database
    private void updateResidentInDatabase(int residentId, String firstName, String middleName, String lastName, String suffix, String address, String dateOfBirth, String contact, String email) {
        // A SQL command to modify "ResidentDB" for a matching "resident_id"
        String query = "UPDATE ResidentDB " +
                       "SET resident_firstName = ?, resident_midName = ?, resident_lastName = ?, resident_suffix = ?, resident_address = ?, resident_DoB = ?, resident_contactNo = ?, resident_email = ? " +
                       "WHERE resident_id = ?";
        
        try (
                Connection connectUpdateResident = getConnection();
                PreparedStatement prepareUpdateResident = connectUpdateResident.prepareStatement(query)
            ) {
            // Convert the text date into a valid SQL date
            java.sql.Date normalizeSQLdate = normalizeDate(dateOfBirth);

            // Fill each question mark with the provided data
            prepareUpdateResident.setString(1, firstName.trim());
            prepareUpdateResident.setString(2, middleName.isEmpty() ? null : middleName.trim());
            prepareUpdateResident.setString(3, lastName.trim());
            prepareUpdateResident.setString(4, suffix.isEmpty() ? null : suffix.trim());
            prepareUpdateResident.setString(5, address.trim());
            prepareUpdateResident.setDate(6, normalizeSQLdate); 
            prepareUpdateResident.setString(7, contact.trim());
            prepareUpdateResident.setString(8, email.trim());
            prepareUpdateResident.setInt(9, residentId);

            // Run the update command
            int rowsUpdated = prepareUpdateResident.executeUpdate();
            // If at least one row changed, success message
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(ResidentForm.this, "Resident updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadResidentData();
            } else {
                JOptionPane.showMessageDialog(ResidentForm.this, "No resident found to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException handleDatabaseUpdateResident) {
            handleDatabaseUpdateResident.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error updating resident: " + handleDatabaseUpdateResident.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException handleParseUpdateResident) {
            handleParseUpdateResident.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Invalid Date format. Please use dd/MM/yyyy.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // "normalizeDate" also turns dd/MM/yyyy into a SQL date. Similar to "convertToDate," but used for updates
    private java.sql.Date normalizeDate(String dateOfBirth) throws ParseException { 
        SimpleDateFormat createFormatDate = new SimpleDateFormat("dd/MM/yyyy");
        createFormatDate.setLenient(false);
        java.util.Date parsedDate = createFormatDate.parse(dateOfBirth);
        return new java.sql.Date(parsedDate.getTime());
    }

    // "loadResidentData" pulls all existing records from "ResidentDB" into the table
    private void loadResidentData() {
        // SQL command to select every column from the ResidentDB
        String query = "SELECT * FROM ResidentDB";

        try (
                Connection connectLoadResident = getConnection();
                Statement statementLoadResident = connectLoadResident.createStatement();
                ResultSet resultLoadResident = statementLoadResident.executeQuery(query)
            ) {
            // Make a "DefaultTableModel" so we can manage rows
            DefaultTableModel loadModel = new DefaultTableModel() {
                // We override "isCellEditable" so user can’t type into table cells directly
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            // Add column headings in the same order as the database
            loadModel.addColumn("Resident ID");
            loadModel.addColumn("First Name");
            loadModel.addColumn("Middle Name");
            loadModel.addColumn("Last Name");
            loadModel.addColumn("Suffix");
            loadModel.addColumn("Address");
            loadModel.addColumn("Date of Birth");
            loadModel.addColumn("Contact Number");
            loadModel.addColumn("Email");

            // Loop over the results
            while (resultLoadResident.next()) {
                // Add each row to the table model
                loadModel.addRow(new Object[]{
                    resultLoadResident.getInt("resident_id"),
                    resultLoadResident.getString("resident_firstName"),
                    resultLoadResident.getString("resident_midName"),
                    resultLoadResident.getString("resident_lastName"),
                    resultLoadResident.getString("resident_suffix"),
                    resultLoadResident.getString("resident_address"),
                    new SimpleDateFormat("dd/MM/yyyy").format(resultLoadResident.getDate("resident_DoB")),
                    resultLoadResident.getString("resident_contactNo"),
                    resultLoadResident.getString("resident_email")
                });
            }

            // Style the table header so it has a colorful background
            residentTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel headerLabel = new JLabel(value.toString());
                    headerLabel.setOpaque(true);
                    headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    // A bright color for the header row
                    headerLabel.setBackground(new Color(255, 104, 101));
                    headerLabel.setForeground(Color.BLACK);
                    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    headerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    return headerLabel;
                }
            });

            // Apply this model to our residentTable
            residentTable.setModel(loadModel); 
            // Hide the ID column so it doesn’t distract the user
            residentTable.getColumnModel().getColumn(0).setMinWidth(0);
            residentTable.getColumnModel().getColumn(0).setMaxWidth(0);
            residentTable.getColumnModel().getColumn(0).setWidth(0);

        } catch (SQLException handleDatabaseLoad) {
            // If the database load fails, print details and show a popup
            handleDatabaseLoad.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error loading data: " + handleDatabaseLoad.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}