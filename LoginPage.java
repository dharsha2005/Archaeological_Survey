import java.awt.*;
import java.awt.event.*;

class LoginPage extends Frame implements ActionListener {
    TextField usernameField, passwordField;
    Button loginButton, signupButton;
    Label messageLabel;

    public LoginPage() {
        setLayout(new GridLayout(5, 1));
        setTitle("Login Page");
        setSize(400, 250);

        // Username and password fields
        usernameField = new TextField(20);
        passwordField = new TextField(20);
        passwordField.setEchoChar('*'); // hide password input

        // Login button
        loginButton = new Button("Login");
        loginButton.addActionListener(this);
        
        // Signup button
        signupButton = new Button("Sign Up");
        signupButton.addActionListener(e -> {
            dispose(); // Close login page
            new SignupPage(); // Open SignupPage
        });

        // Message label
        messageLabel = new Label();

        add(new Label("Username:"));
        add(usernameField);
        add(new Label("Password:"));
        add(passwordField);
        add(loginButton);
        add(signupButton);
        add(messageLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Use the validateUser method to check credentials
        if (SignupPage.validateUser(username, password)) {
            messageLabel.setText("Login successful!");
            dispose(); // Close login page
            new TicketBookingSystem(); // Open TicketBookingSystem
        } else {
            messageLabel.setText("Invalid credentials. Try again.");
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
