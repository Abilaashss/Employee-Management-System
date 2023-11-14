import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeManagementSystem extends JFrame {
    private JTextField empIdField, empNameField, ageField, sexField, deptField, salaryField, phoneNoField;
    private JTextArea displayArea;
    private JTextArea leaveReasonArea;

    public EmployeeManagementSystem() {
        setTitle("Employee Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
        layoutComponents();
//        leaveReasonArea = new JTextArea();
//        leaveReasonArea.setBackground(new Color(108, 122, 137)); // Dark Gray Background
//        leaveReasonArea.setForeground(Color.WHITE);
//        leaveReasonArea.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 1));
    }

    private void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBackground(new Color(34, 34, 34));
        leaveReasonArea = new JTextArea();
        leaveReasonArea.setBackground(new Color(108, 122, 137)); // Dark Gray Background
        leaveReasonArea.setForeground(Color.WHITE);
        leaveReasonArea.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 1));

        empIdField = createStyledTextField();
        empNameField = createStyledTextField();
        ageField = createStyledTextField();
        sexField = createStyledTextField();
        deptField = createStyledTextField();
        salaryField = createStyledTextField();
        phoneNoField = createStyledTextField();
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(34, 34, 34)); // Darker Blue Background
        displayArea.setForeground(Color.WHITE);

        JPanel leaveReasonPanel = new JPanel(new BorderLayout());
        leaveReasonPanel.add(new JLabel("Leave Reason:"), BorderLayout.WEST);
        leaveReasonPanel.add(leaveReasonArea, BorderLayout.CENTER);
        leaveReasonPanel.setBackground(new Color(41, 128, 185)); // Dark Blue Background
        panel.add(leaveReasonPanel);


        panel.add(createLabelAndFieldPanel("Employee ID : ", empIdField));
        panel.add(createLabelAndFieldPanel("Employee Name : ", empNameField));
        panel.add(createLabelAndFieldPanel("Age : ", ageField));
        panel.add(createLabelAndFieldPanel("Gender : ", sexField));
        panel.add(createLabelAndFieldPanel("Department : ", deptField));
        panel.add(createLabelAndFieldPanel("Salary : ", salaryField));
        panel.add(createLabelAndFieldPanel("Phone No : ", phoneNoField));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(34, 34, 34)); // Darker Blue Background

        JButton addEmployeeBtn = createModernButton("Add Employee");
        JButton editEmployeeBtn = createModernButton("Edit Employee");
        JButton deleteEmployeeBtn = createModernButton("Delete Employee");
        JButton viewEmployeeBtn = createModernButton("View Employee");
        JButton viewAttendanceBtn = createModernButton("View Attendance"); // Added viewAttendanceBtn
        JButton fillAttendanceBtn = createModernButton("Fill Attendance");
        JButton leaveApplicationBtn = createModernButton("Leave Application");

        addEmployeeBtn.addActionListener(e -> showAddEmployeePage());
        editEmployeeBtn.addActionListener(e -> editEmployee());
        deleteEmployeeBtn.addActionListener(e -> deleteEmployee());
        viewEmployeeBtn.addActionListener(e -> viewEmployee());
        viewAttendanceBtn.addActionListener(e -> viewAttendance()); // Added action for viewAttendanceBtn
        fillAttendanceBtn.addActionListener(e -> fillAttendance());
        leaveApplicationBtn.addActionListener(e -> leaveApplication());

        buttonPanel.add(addEmployeeBtn);
        buttonPanel.add(editEmployeeBtn);
        buttonPanel.add(deleteEmployeeBtn);
        buttonPanel.add(viewEmployeeBtn);
        buttonPanel.add(viewAttendanceBtn); // Added viewAttendanceBtn to buttonPanel
        buttonPanel.add(fillAttendanceBtn);
        buttonPanel.add(leaveApplicationBtn);

        panel.add(buttonPanel);

        add(panel);

        addEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddEmployeePage();
            }
        });

        editEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editEmployee();
            }
        });

        deleteEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        viewEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEmployee();
            }
        });

        viewAttendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAttendance();
            }
        });

        fillAttendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillAttendance();
            }
        });

        panel.add(addEmployeeBtn);
        panel.add(editEmployeeBtn);
        panel.add(deleteEmployeeBtn);
        panel.add(viewEmployeeBtn);
        panel.add(viewAttendanceBtn); // Added viewAttendanceBtn to panel
        panel.add(fillAttendanceBtn);
        panel.add(leaveApplicationBtn);

        add(panel);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(108, 122, 137)); // Dark Gray Background
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 1));
        return textField;
    }

    private JPanel createLabelAndFieldPanel(String label, JTextField textField) {
        JPanel labelAndFieldPanel = new JPanel(new BorderLayout());
        labelAndFieldPanel.add(new JLabel(label, SwingConstants.RIGHT), BorderLayout.WEST);
        labelAndFieldPanel.add(textField, BorderLayout.CENTER);
        labelAndFieldPanel.setBackground(new Color(41, 128, 185)); // Dark Blue Background
        return labelAndFieldPanel;
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(39, 174, 96)); // Green Color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        // Add an empty border
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        return button;
    }

    private void layoutComponents() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/employeesdb";
        String username = "root";
        String password = "@Bik@boi140505";
        return DriverManager.getConnection(url, username, password);
    }

    private void showAddEmployeePage() {
        JPanel addEmployeePanel = new JPanel();
        addEmployeePanel.setLayout(new GridLayout(8, 2, 10, 10));

        addEmployeePanel.add(new JLabel("Employee ID:"));
        addEmployeePanel.add(empIdField);
        addEmployeePanel.add(new JLabel("Employee Name:"));
        addEmployeePanel.add(empNameField);
        addEmployeePanel.add(new JLabel("Age:"));
        addEmployeePanel.add(ageField);
        addEmployeePanel.add(new JLabel("Gender:"));
        addEmployeePanel.add(sexField);
        addEmployeePanel.add(new JLabel("Department:"));
        addEmployeePanel.add(deptField);
        addEmployeePanel.add(new JLabel("Salary:"));
        addEmployeePanel.add(salaryField);
        addEmployeePanel.add(new JLabel("Phone No:"));
        addEmployeePanel.add(phoneNoField);

        int result = JOptionPane.showConfirmDialog(this, addEmployeePanel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            addEmployeeToDatabase();
        }
    }

    private void addEmployeeToDatabase() {
        try (Connection connection = getConnection()) {
            String empId = empIdField.getText();
            String empName = empNameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String sex = sexField.getText();
            String dept = deptField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            String phoneNo = phoneNoField.getText();

            String sql = "INSERT INTO employees (empid, empname, age, sex, dept, salary, phoneno) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, empId);
                statement.setString(2, empName);
                statement.setInt(3, age);
                statement.setString(4, sex);
                statement.setString(5, dept);
                statement.setDouble(6, salary);
                statement.setString(7, phoneNo);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage());
        }
    }

    private void editEmployee() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID to edit:");
        if (empId != null && !empId.isEmpty()) {
            try (Connection connection = getConnection()) {
                String sql = "SELECT * FROM employees WHERE empid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, empId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            empIdField.setText(resultSet.getString("empid"));
                            empNameField.setText(resultSet.getString("empname"));
                            ageField.setText(String.valueOf(resultSet.getInt("age")));
                            sexField.setText(resultSet.getString("sex"));
                            deptField.setText(resultSet.getString("dept"));
                            salaryField.setText(String.valueOf(resultSet.getDouble("salary")));
                            phoneNoField.setText(resultSet.getString("phoneno"));

                            JPanel editEmployeePanel = new JPanel();
                            editEmployeePanel.setLayout(new GridLayout(8, 2, 10, 10));

                            editEmployeePanel.add(new JLabel("Employee ID:"));
                            editEmployeePanel.add(empIdField);
                            editEmployeePanel.add(new JLabel("Employee Name:"));
                            editEmployeePanel.add(empNameField);
                            editEmployeePanel.add(new JLabel("Age:"));
                            editEmployeePanel.add(ageField);
                            editEmployeePanel.add(new JLabel("Gender:"));
                            editEmployeePanel.add(sexField);
                            editEmployeePanel.add(new JLabel("Department:"));
                            editEmployeePanel.add(deptField);
                            editEmployeePanel.add(new JLabel("Salary:"));
                            editEmployeePanel.add(salaryField);
                            editEmployeePanel.add(new JLabel("Phone No:"));
                            editEmployeePanel.add(phoneNoField);

                            int result = JOptionPane.showConfirmDialog(this, editEmployeePanel, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                updateEmployee(empId);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Employee not found!");
                        }
                    }
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error editing employee: " + e.getMessage());
            }
        }
    }

    private void updateEmployee(String empId) {
        try (Connection connection = getConnection()) {
            String empName = empNameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String sex = sexField.getText();
            String dept = deptField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            String phoneNo = phoneNoField.getText();

            String sql = "UPDATE employees SET empname = ?, age = ?, sex = ?, dept = ?, salary = ?, phoneno = ? WHERE empid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, empName);
                statement.setInt(2, age);
                statement.setString(3, sex);
                statement.setString(4, dept);
                statement.setDouble(5, salary);
                statement.setString(6, phoneNo);
                statement.setString(7, empId);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID to delete:");
        if (empId != null && !empId.isEmpty()) {
            try (Connection connection = getConnection()) {
                String sql = "DELETE FROM employees WHERE empid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, empId);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee not found or error deleting employee");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage());
            }
        }
    }

    private void fillAttendance() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID for attendance:");
        if (empId != null && !empId.isEmpty()) {
            try (Connection connection = getConnection()) {
                String sql = "INSERT INTO attendance (empid, timestamp) VALUES (?, NOW())";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, empId);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Attendance filled successfully!");
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error filling attendance: " + e.getMessage());
            }
        }
    }

    private void viewEmployee() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID to view:");
        if (empId != null && !empId.isEmpty()) {
            try (Connection connection = getConnection()) {
                String sql = "SELECT * FROM employees WHERE empid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, empId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            StringBuilder details = new StringBuilder();
                            details.append("Employee ID: ").append(resultSet.getString("empid")).append("\n");
                            details.append("Employee Name: ").append(resultSet.getString("empname")).append("\n");
                            details.append("Age: ").append(resultSet.getInt("age")).append("\n");
                            details.append("Gender: ").append(resultSet.getString("sex")).append("\n");
                            details.append("Department: ").append(resultSet.getString("dept")).append("\n");
                            details.append("Salary: ").append(resultSet.getDouble("salary")).append("\n");
                            details.append("Phone No: ").append(resultSet.getString("phoneno")).append("\n");

                            JOptionPane.showMessageDialog(this, details.toString(), "Employee Details", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Employee not found!");
                        }
                    }
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error viewing employee: " + e.getMessage());
            }
        }
    }
    private void viewAttendance() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID to view attendance:");
        if (empId != null && !empId.isEmpty()) {
            try (Connection connection = getConnection()) {
                String sql = "SELECT * FROM attendance WHERE empid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, empId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        StringBuilder attendanceDetails = new StringBuilder("Attendance Details:\n");

                        while (resultSet.next()) {
                            attendanceDetails.append("Timestamp: ").append(resultSet.getString("timestamp")).append("\n");
                        }

                        if (attendanceDetails.length() > 0) {
                            JOptionPane.showMessageDialog(this, attendanceDetails.toString(), "Attendance Details", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "No attendance records found for the employee.");
                        }
                    }
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error viewing attendance: " + e.getMessage());
            }
        }
    }
    private void leaveApplication() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID for leave application:");
        if (empId != null && !empId.isEmpty()) {
            String leaveReason = promptForLeaveReason();
            if (leaveReason != null) {
                try (Connection connection = getConnection()) {
                    String sql = "INSERT INTO leave_applications (empid, applied_date, reason) VALUES (?, NOW(), ?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, empId);
                        statement.setString(2, leaveReasonArea.getText());
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Leave application submitted successfully!");
                    }
                } catch (SQLException | NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error submitting leave application: " + e.getMessage());
                }
            }
        }
    }

    private String promptForLeaveReason() {
        JPanel leaveReasonPanel = new JPanel();
        leaveReasonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        leaveReasonPanel.add(new JLabel("Leave Reason:"));
        leaveReasonPanel.add(leaveReasonArea);

        int result = JOptionPane.showConfirmDialog(this, leaveReasonPanel, "Leave Reason", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return leaveReasonArea.getText();
        }
        return null;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagementSystem());
    }
}
