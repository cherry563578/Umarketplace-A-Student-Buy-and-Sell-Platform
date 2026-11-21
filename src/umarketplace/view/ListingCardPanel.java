package umarketplace.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import umarketplace.model.Listing;

public class ListingCardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String CARD_BACKGROUND_PATH = "ListingCardPanel.png";

    private final Image cardBackgroundImage;

    public final JLabel imageLabel = new JLabel();
    public final JLabel categoryLabel = new JLabel();
    public final JLabel titleLabel = new JLabel();
    public final JLabel statusLabel = new JLabel();
    public final JLabel priceLabel = new JLabel();
    public final JButton contactButton = new JButton();

    public ListingCardPanel(int width, int height) {
        setLayout(null);
        setSize(width, height);
        setOpaque(false);

        this.cardBackgroundImage = loadImage(CARD_BACKGROUND_PATH);

        Font regularFont = loadCustomFont("HelveticaNowDisplay-Regular.ttf", Font.PLAIN, 13);
        Font boldFont = loadCustomFont("HelveticaNowDisplay-Bold.ttf", Font.PLAIN, 18);

        int padX = 14;
        int innerW = width - (padX * 2);

        // TOP LISTING PHOTO AREA
        imageLabel.setBounds(1, 0, width - 2, 200);
        imageLabel.setOpaque(false);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(imageLabel);

        // CATEGORY LABEL
        categoryLabel.setFont(regularFont.deriveFont(Font.PLAIN, 13f));
        categoryLabel.setForeground(new Color(128, 128, 128));
        categoryLabel.setBounds(padX, 208, innerW, 18);
        add(categoryLabel);

        // TITLE LABEL
        titleLabel.setFont(boldFont.deriveFont(Font.PLAIN, 18f));
        titleLabel.setForeground(Color.decode("#af2532"));
        titleLabel.setBounds(padX, 226, innerW, 20);
        add(titleLabel);

        // STATUS LABEL
        statusLabel.setFont(boldFont.deriveFont(Font.PLAIN, 15f));
        statusLabel.setForeground(Color.decode("#af2532"));
        statusLabel.setBounds(padX, 245, innerW - 44, 18);
        add(statusLabel);

        // PRICE LABEL
        priceLabel.setFont(boldFont.deriveFont(Font.PLAIN, 28f));
        priceLabel.setForeground(Color.decode("#af2532"));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setBounds(padX, 272, innerW, 34);
        add(priceLabel);
        
        // CONTACT TRIGGER
        contactButton.setUI(new BasicButtonUI());
        contactButton.setOpaque(false);
        contactButton.setContentAreaFilled(false);
        contactButton.setBorderPainted(false);
        contactButton.setFocusPainted(false);
        contactButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactButton.setBounds(width - 46, 206, 38, 38);
        add(contactButton);
        setComponentZOrder(contactButton, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cardBackgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(cardBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2.dispose();
        }
    }

    private Image loadImage(String path) {
        ImageIcon raw = new ImageIcon(path);
        if (raw.getIconWidth() > 0) {
            return raw.getImage();
        }
        String normalized = path.startsWith("/") ? path : "/" + path;
        URL url = getClass().getResource(normalized);
        if (url != null) {
            ImageIcon fromResource = new ImageIcon(url);
            if (fromResource.getIconWidth() > 0) {
                return fromResource.getImage();
            }
        }
        return null;
    }

    public void bindListing(Listing item, ImageIcon scaledIcon) {
        categoryLabel.setText(item.getCategory());
        titleLabel.setText(item.getTitle());

        Font f = statusLabel.getFont();
        String fontFamily = (f != null) ? f.getFamily().replace("'", "") : "SansSerif";
        String hexColor = String.format("#%06x", statusLabel.getForeground().getRGB() & 0xFFFFFF);

        statusLabel.setText(
            "<html><span style='font-family:\"Segoe UI Symbol\",SansSerif; color:" + hexColor + ";'>&#9432;</span> "
            + "<span style='font-family:\"" + fontFamily + "\",SansSerif; color:" + hexColor + ";'>"
            + escapeCardHtml(item.getStatus())
            + "</span></html>"
        );

        priceLabel.setText(String.format("PHP %,.0f", item.getPrice()));
        imageLabel.setIcon(scaledIcon);
    }

    private String escapeCardHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public void onTitleClick(Runnable action) {
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && isVisible()) {
                    action.run();
                }
            }
        });
    }

    public void onPriceClick(Runnable action) {
        priceLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        priceLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && isVisible()) {
                    action.run();
                }
            }
        });
    }
    
    private Font loadCustomFont(String path, int style, float size) {
        try {
            Font loaded = null;
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                loaded = Font.createFont(Font.TRUETYPE_FONT, file);
            } else {
                String normalized = path.startsWith("/") ? path : "/" + path;
                java.net.URL url = getClass().getResource(normalized);
                if (url != null) {
                    try (java.io.InputStream is = url.openStream()) {
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
}