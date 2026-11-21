package umarketplace.view;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.swing.*;

public class LoginView extends JFrame {

    public JTextField studentIdOrEmailField;
    public JPasswordField passwordField;
    public JButton loginButton;
    public JButton createAccountButton;

    private static final String ID_PLACEHOLDER = "Student ID or Email";
    private static final String PASSWORD_PLACEHOLDER = "Password";
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color PLACEHOLDER_COLOR = new Color(130, 136, 142);

    public LoginView() {
        setTitle("UMarketplace");
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font regularFont = loadCustomFont("HelveticaNowDisplay-Regular.ttf", Font.PLAIN, 15f);

        BackgroundPanel bgPanel = new BackgroundPanel("UMarketplace.jpg");
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // --- STUDENT ID / EMAIL FIELD ---
        studentIdOrEmailField = new JTextField();
        studentIdOrEmailField.setFont(regularFont);
        studentIdOrEmailField.setOpaque(false);
        studentIdOrEmailField.setBackground(new Color(0, 0, 0, 0));
        studentIdOrEmailField.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
        studentIdOrEmailField.setBounds(808, 247, 404, 34);
        configureTextPlaceholder(studentIdOrEmailField, ID_PLACEHOLDER);
        bgPanel.add(studentIdOrEmailField);

        // --- PASSWORD FIELD ---
        passwordField = new JPasswordField();
        passwordField.setFont(regularFont);
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
        passwordField.setBounds(808, 303, 404, 34);
        configurePasswordPlaceholder(passwordField, PASSWORD_PLACEHOLDER);
        bgPanel.add(passwordField);

        // --- LOGIN BUTTON ---
        loginButton = new JButton();
        loginButton.setBounds(814, 350, 412, 40);
        makeTransparentButton(loginButton);
        bgPanel.add(loginButton);

        // --- CREATE A NEW ACCOUNT BUTTON ---
        createAccountButton = new JButton();
        createAccountButton.setBounds(935, 466, 175, 26);
        makeTransparentButton(createAccountButton);
        bgPanel.add(createAccountButton);
    }

    private Font loadCustomFont(String path, int style, float size) {
        try {
            Font loaded = null;
            File file = new File(path);
            if (file.exists()) {
                loaded = Font.createFont(Font.TRUETYPE_FONT, file);
            } else {
                String normalized = path.startsWith("/") ? path : "/" + path;
                URL url = getClass().getResource(normalized);
                if (url != null) {
                    try (InputStream is = url.openStream()) {
                        loaded = Font.createFont(Font.TRUETYPE_FONT, is);
                    }
                }
            }
            if (loaded != null) {
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(loaded);
                return loaded.deriveFont(style, size);
            }
        } catch (Exception ex) {
            // if font file is missing or unreadable
        }
        return new Font("SansSerif", style, Math.round(size));
    }

    private void configureTextPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(PLACEHOLDER_COLOR);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
    }

    private void configurePasswordPlaceholder(JPasswordField field, String placeholder) {
        char defaultEchoChar = field.getEchoChar();
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setForeground(PLACEHOLDER_COLOR);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String current = new String(field.getPassword());
                if (current.equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String current = new String(field.getPassword());
                if (current.isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                } else {
                    field.setEchoChar(defaultEchoChar != 0 ? defaultEchoChar : '•');
                }
            }
        });
    }

    public String getStudentIdOrEmailInput() {
        String text = studentIdOrEmailField.getText().trim();
        return text.equals(ID_PLACEHOLDER) ? "" : text;
    }

    public String getPasswordInput() {
        String text = new String(passwordField.getPassword());
        return text.equals(PASSWORD_PLACEHOLDER) ? "" : text;
    }

    private void makeTransparentButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}