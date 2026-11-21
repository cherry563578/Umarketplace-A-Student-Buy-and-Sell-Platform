package umarketplace.controller;

import javax.swing.JOptionPane;
import umarketplace.model.ListingFileStore;
import umarketplace.view.HomepageView;
import umarketplace.view.LoginView;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;

        this.view.loginButton.addActionListener(e -> handleLogin());
        this.view.createAccountButton.addActionListener(e -> handleCreateAccount());

        this.view.studentIdOrEmailField.addActionListener(e -> handleLogin());
        this.view.passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String identifier = view.getStudentIdOrEmailInput();
        String password = view.getPasswordInput();

        if (identifier.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                view,
                "Please enter both your Student ID or Email and Password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        HomepageView homeView = new HomepageView();
        new HomepageController(homeView, new ListingFileStore("data/listings.txt"));
        homeView.setVisible(true);
        view.dispose();
    }

    private void handleCreateAccount() {
        JOptionPane.showMessageDialog(
            view,
            "Create a new account clicked.",
            "Registration",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}