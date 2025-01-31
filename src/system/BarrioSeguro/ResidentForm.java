package system.BarrioSeguro;

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

public class ResidentForm extends BaseForm {

    private JTable residentTable;
    private JTextField searchTextField;

    public ResidentForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Resident Database");
        initialize();
        loadResidentData();
    }

    private void initialize() {
        JLayeredPane residentPane = new JLayeredPane();
        setContentPane(residentPane);

        addBackgroundImage(residentPane);
        addDashboardPanel(residentPane);
        addResidentPanel(residentPane);
    }

    private void addResidentToDatabase(String firstName, String middleName, String lastName, String suffix, String address, String dateOfBirth, String contact, String email) {
        String query = "INSERT INTO ResidentDB (resident_firstName, resident_midName, resident_lastName, resident_suffix, resident_address, resident_DoB, resident_contactNo, resident_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (
            Connection connectAddResident = getConnection();
            PreparedStatement prepareAddResident = connectAddResident.prepareStatement(query)
        ) {
            java.sql.Date convertSQLdate = convertToDate(dateOfBirth);
            
            prepareAddResident.setString(1, firstName);
            prepareAddResident.setString(2, middleName.isEmpty() ? null : middleName);
            prepareAddResident.setString(3, lastName);
            prepareAddResident.setString(4, suffix.isEmpty() ? null : suffix);
            prepareAddResident.setString(5, address);
            prepareAddResident.setDate(6, convertSQLdate);
            prepareAddResident.setString(7, contact);
            prepareAddResident.setString(8, email);
            
            int rowsInserted = prepareAddResident.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(ResidentForm.this, "Resident added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadResidentData();
            }
        } catch (ParseException handleParseAddResident) {
            JOptionPane.showMessageDialog(ResidentForm.this, "Invalid Date of Birth format. Please use dd/MM/yyyy.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException handleDatabaseAddResident) {
            handleDatabaseAddResident.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error adding resident: " + handleDatabaseAddResident.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private java.sql.Date convertToDate(String dateOfBirth) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        java.util.Date parsedDate = inputFormat.parse(dateOfBirth);
        return new java.sql.Date(parsedDate.getTime());
    }
        
    private void addResidentPanel(JLayeredPane residentPane) {
        JPanel addingResidentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        addingResidentPanel.setBounds(480, 185, 895, 722);
        addingResidentPanel.setBackground(new Color(102, 77, 77, 178)); 
        addingResidentPanel.setLayout(null);
        addingResidentPanel.setOpaque(false);

        residentPane.add(addingResidentPanel, JLayeredPane.PALETTE_LAYER);
        
        residentTable = new JTable();
        JScrollPane scrollResidentTable = new JScrollPane(residentTable);
        scrollResidentTable.setBounds(22, 93, 848, 483);
        addingResidentPanel.add(scrollResidentTable);;
        
        searchTextField = createRoundedTextField("Search", 25);
        searchTextField.setToolTipText("");
        searchTextField.setHorizontalAlignment(SwingConstants.LEFT);
        searchTextField.setForeground(Color.LIGHT_GRAY);
        searchTextField.setBackground(new Color(254, 254, 254));
        searchTextField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchTextField.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchTextField.setBounds(22, 45, 216, 37);
        
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
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Search");
                    searchTextField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent eventKeyForSearchField) {
                String searchQuery = searchTextField.getText().trim().toLowerCase();
                filterTable(searchQuery);
            }
        });

        addingResidentPanel.add(searchTextField);

        JButton btnAdd = new JButton("Add");
        styleRoundedButton(btnAdd);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForAddBtn) {
                JPanel addResidentInfoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
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

                int result = JOptionPane.showConfirmDialog(null, addResidentInfoPanel, "Add Resident",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameField.getText().trim();
                    String middleName = middleNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String suffix = suffixField.getText().trim();
                    String address = addressField.getText().trim();
                    String dateOfBirth = dobField.getText().trim();
                    String contact = contactField.getText().trim();
                    String email = emailField.getText().trim();

                    if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty() || contact.isEmpty() || email.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!contact.matches("\\d{11}")) {
                        JOptionPane.showMessageDialog(null, "Contact number must be 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!dateOfBirth.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        JOptionPane.showMessageDialog(null, "Date of Birth must be in the format dd/mm/yyyy.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    addResidentToDatabase(firstName, middleName, lastName, suffix, address, dateOfBirth, contact, email);
                }
            }
        });
        btnAdd.setBounds(68, 612, 147, 64);
        addingResidentPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        styleRoundedButton(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForUpdateBtn) {
                int selectedRow = residentTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a resident to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int residentId = (Integer) residentTable.getValueAt(selectedRow, 0);
                String firstName = (String) residentTable.getValueAt(selectedRow, 1);
                String middleName = (String) residentTable.getValueAt(selectedRow, 2);
                String lastName = (String) residentTable.getValueAt(selectedRow, 3);
                String suffix = (String) residentTable.getValueAt(selectedRow, 4);
                String address = (String) residentTable.getValueAt(selectedRow, 5);
                String dateOfBirth = residentTable.getValueAt(selectedRow, 6).toString();
                String contact = (String) residentTable.getValueAt(selectedRow, 7);
                String email = (String) residentTable.getValueAt(selectedRow, 8);

                JPanel updateResidentInfoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                JTextField firstNameField = new JTextField(firstName);
                JTextField middleNameField = new JTextField(middleName);
                JTextField lastNameField = new JTextField(lastName);
                JTextField suffixField = new JTextField(suffix);
                JTextField addressField = new JTextField(address);
                JTextField dobField = new JTextField(dateOfBirth);
                JTextField contactField = new JTextField(contact);
                JTextField emailField = new JTextField(email);

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

                int result = JOptionPane.showConfirmDialog(null, updateResidentInfoPanel, "Update Resident", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String updatedFirstName = firstNameField.getText().trim();
                    String updatedMiddleName = middleNameField.getText().trim();
                    String updatedLastName = lastNameField.getText().trim();
                    String updatedSuffix = suffixField.getText().trim();
                    String updatedAddress = addressField.getText().trim();
                    String updatedDob = dobField.getText().trim();
                    String updatedContact = contactField.getText().trim();
                    String updatedEmail = emailField.getText().trim();

                    try {
                        updateResidentInDatabase(residentId, updatedFirstName, updatedMiddleName, updatedLastName, updatedSuffix, updatedAddress, updatedDob, updatedContact, updatedEmail);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error updating resident: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnUpdate.setBounds(268, 612, 147, 62);
        addingResidentPanel.add(btnUpdate);

        JButton btnDel = new JButton("Delete");
        styleRoundedButton(btnDel);
        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForDeleteBtn) {
                int selectedRow = residentTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a resident to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int residentId = (int) residentTable.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this resident?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteResidentFromDatabase(residentId);
                }
            }
        });
        btnDel.setBounds(468, 612, 147, 62);
        addingResidentPanel.add(btnDel);

        /* In progress... just create a fake function, then notify when the ID with its details is actually going to be printed. */
        JButton btnPrintID = new JButton("Print ID");
        styleRoundedButton(btnPrintID);
        btnPrintID.setBounds(668, 612, 147, 62);
        addingResidentPanel.add(btnPrintID);
    }
    
    private void filterTable(String searchQuery) {
        DefaultTableModel filterModel = (DefaultTableModel) residentTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(filterModel);
        residentTable.setRowSorter(sorter);

        if (searchQuery.isEmpty() || searchQuery.equalsIgnoreCase("search")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery));
        }
    }

    private void deleteResidentFromDatabase(int residentId) {
        String query = "DELETE FROM ResidentDB WHERE resident_id = ?";

        try (
                Connection connectDeleteResident = getConnection();
                PreparedStatement prepareDeleteResident = connectDeleteResident.prepareStatement(query)
            ) {
            prepareDeleteResident.setInt(1, residentId);

            int rowsDeleted = prepareDeleteResident.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(ResidentForm.this, "Resident deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadResidentData();
            } else {
                JOptionPane.showMessageDialog(ResidentForm.this, "No resident found to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException handleDatabaseDeleteResident) {
            handleDatabaseDeleteResident.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error deleting resident: " + handleDatabaseDeleteResident.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateResidentInDatabase(int residentId, String firstName, String middleName, String lastName, String suffix, String address, String dateOfBirth, String contact, String email) {
        String query = "UPDATE ResidentDB " +
                       "SET resident_firstName = ?, resident_midName = ?, resident_lastName = ?, resident_suffix = ?, resident_address = ?, resident_DoB = ?, resident_contactNo = ?, resident_email = ? " +
                       "WHERE resident_id = ?";
        
        try (
                Connection connectUpdateResident = getConnection();
                PreparedStatement prepareUpdateResident = connectUpdateResident.prepareStatement(query)
            ) {
            java.sql.Date normalizeSQLdate = normalizeDate(dateOfBirth);

            prepareUpdateResident.setString(1, firstName.trim());
            prepareUpdateResident.setString(2, middleName.isEmpty() ? null : middleName.trim());
            prepareUpdateResident.setString(3, lastName.trim());
            prepareUpdateResident.setString(4, suffix.isEmpty() ? null : suffix.trim());
            prepareUpdateResident.setString(5, address.trim());
            prepareUpdateResident.setDate(6, normalizeSQLdate); 
            prepareUpdateResident.setString(7, contact.trim());
            prepareUpdateResident.setString(8, email.trim());
            prepareUpdateResident.setInt(9, residentId);

            int rowsUpdated = prepareUpdateResident.executeUpdate();
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
    
    private java.sql.Date normalizeDate(String dateOfBirth) throws ParseException {
        SimpleDateFormat createFormatDate = new SimpleDateFormat("dd/MM/yyyy");
        createFormatDate.setLenient(false);
        java.util.Date parsedDate = createFormatDate.parse(dateOfBirth);
        return new java.sql.Date(parsedDate.getTime());
    }

    private void loadResidentData() {
        String query = "SELECT * FROM ResidentDB";

        try (
                Connection connectLoadResident = getConnection();
                Statement statementLoadResident = connectLoadResident.createStatement();
                ResultSet resultLoadResident = statementLoadResident.executeQuery(query)
            ) {

            DefaultTableModel loadModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            loadModel.addColumn("Resident ID");
            loadModel.addColumn("First Name");
            loadModel.addColumn("Middle Name");
            loadModel.addColumn("Last Name");
            loadModel.addColumn("Suffix");
            loadModel.addColumn("Address");
            loadModel.addColumn("Date of Birth");
            loadModel.addColumn("Contact Number");
            loadModel.addColumn("Email");

            while (resultLoadResident.next()) {
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

            residentTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel headerLabel = new JLabel(value.toString());
                    headerLabel.setOpaque(true);
                    headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    headerLabel.setBackground(new Color(255, 104, 101));
                    headerLabel.setForeground(Color.BLACK);
                    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    headerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    return headerLabel;
                }
            });

            residentTable.setModel(loadModel); 
            residentTable.getColumnModel().getColumn(0).setMinWidth(0);
            residentTable.getColumnModel().getColumn(0).setMaxWidth(0);
            residentTable.getColumnModel().getColumn(0).setWidth(0);

        } catch (SQLException handleDatabaseLoad) {
            handleDatabaseLoad.printStackTrace();
            JOptionPane.showMessageDialog(ResidentForm.this, "Error loading data: " + handleDatabaseLoad.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}