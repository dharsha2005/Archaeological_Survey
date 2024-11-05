import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

class SignupPage extends Frame implements ActionListener {
    TextField usernameField, passwordField;
    Button signupButton;
    Label messageLabel;

    // Store user credentials privately
    private static HashMap<String, String> users = new HashMap<>();

    public SignupPage() {
        setLayout(new GridLayout(4, 1));
        setTitle("Signup Page");
        setSize(400, 200);

        // Username and password fields
        usernameField = new TextField(20);
        passwordField = new TextField(20);
        passwordField.setEchoChar('*'); // hide password input

        // Signup button
        signupButton = new Button("Sign Up");
        signupButton.addActionListener(this);

        // Message label
        messageLabel = new Label();

        add(new Label("Choose a Username:"));
        add(usernameField);
        add(new Label("Choose a Password:"));
        add(passwordField);
        add(signupButton);
        add(messageLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if username already exists
        if (users.containsKey(username)) {
            messageLabel.setText("Username already exists. Choose another.");
        } else {
            // Store the new user's credentials
            users.put(username, password);
            messageLabel.setText("Signup successful! You can now login.");
            dispose();
            new LoginPage();
        }
    }

    public static boolean validateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static void main(String[] args) {
        new SignupPage();
    }
}
