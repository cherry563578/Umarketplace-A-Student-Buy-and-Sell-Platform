package umarketplace;

import javax.swing.SwingUtilities;
import umarketplace.view.LoginView;
import umarketplace.controller.LoginController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView view = new LoginView();
            new LoginController(view);
            view.setVisible(true);
        });
    }
}