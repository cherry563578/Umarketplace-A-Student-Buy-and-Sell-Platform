package umarketplace.view;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import umarketplace.model.Listing;

public class HomepageView extends JFrame {

    private static final long serialVersionUID = 1L;
    public static final int ITEMS_PER_PAGE = 5;

    // Top Navigation & Search Controls
    public JButton homeButton;
    public JButton notifButton;
    public JButton profileButton;
    public JButton helpButton;
    public JTextField searchField;
    public JButton searchButton;

    // Filter & Category Controls
    public JButton priceFilterButton;
    public JButton booksButton;
    public JButton uniformsButton;
    public JButton schoolSuppliesButton;
    public JButton electronicsButton;
    public JButton othersButton;

    // 5 Reusable Listing Card Panels (204 x 311 px)
    public final List<ListingCardPanel> listingCards = new ArrayList<>();

    // Pagination Controls (<<, 1..5, >>)
    public JButton prevPageButton;
    public JButton nextPageButton;
    public final List<JButton> pageButtons = new ArrayList<>();

    private JPopupMenu pricePopupMenu;
    private long lastPopupHideTime = 0;
    private final Font customBaseFont;

    private Font loadCustomFont(String path) {
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
                return loaded;
            }
        } catch (Exception ex) {
           
        }
        return null;
    }

    private Font resolveUiFont(int style, int size) {
        if (customBaseFont != null) {
            return customBaseFont.deriveFont(style, (float) size);
        }
        Font base = UIManager.getFont("Label.font");
        if (base != null) {
            return base.deriveFont(style, (float) size);
        }
        return new Font("SansSerif", style, size);
    }

    public HomepageView() {
        this.customBaseFont = loadCustomFont("HelveticaNowDisplay-Regular.ttf");

        setTitle("UMarketplace - Homepage");
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bgPanel = new BackgroundPanel("UMarketplaceHome.jpg");
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // --- TOP NAV BUTTONS ---
        homeButton = createTransparentButton();
        homeButton.setBounds(560, 50, 46, 44);
        bgPanel.add(homeButton);

        notifButton = createTransparentButton();
        notifButton.setBounds(630, 50, 58, 44);
        bgPanel.add(notifButton);

        profileButton = createTransparentButton();
        profileButton.setBounds(695, 50, 65, 44);
        bgPanel.add(profileButton);

        helpButton = createTransparentButton();
        helpButton.setBounds(1168, 40, 70, 54);
        bgPanel.add(helpButton);

        // --- SEARCH FIELD & SEARCH BUTTON ---
        searchField = new JTextField();
        Font searchFont = (customBaseFont != null)
            ? customBaseFont.deriveFont(Font.PLAIN, 14f)
            : resolveUiFont(Font.PLAIN, 14);
        searchField.setFont(searchFont);
        searchField.setOpaque(false);
        searchField.setBackground(new Color(0, 0, 0, 0));
        searchField.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 10));
        searchField.setBounds(302, 142, 830, 34);
        bgPanel.add(searchField);

        searchButton = createTransparentButton();
        searchButton.setBounds(1114, 140, 44, 38);
        bgPanel.add(searchButton);

        // --- LEFT FILTER ("Price +") TRIGGER ---
        priceFilterButton = createTransparentButton();
        priceFilterButton.setBounds(74, 195, 195, 34);
        bgPanel.add(priceFilterButton);

        // --- CATEGORY STRIP HOTSPOTS ---
        booksButton = createTransparentButton();
        booksButton.setBounds(351, 201, 88, 30);

        uniformsButton = createTransparentButton();
        uniformsButton.setBounds(478, 201, 111, 30);

        schoolSuppliesButton = createTransparentButton();
        schoolSuppliesButton.setBounds(626, 201, 155, 30);

        electronicsButton = createTransparentButton();
        electronicsButton.setBounds(820, 201, 137, 30);

        othersButton = createTransparentButton();
        othersButton.setBounds(987, 201, 130, 30);

        bgPanel.add(booksButton);
        bgPanel.add(uniformsButton);
        bgPanel.add(schoolSuppliesButton);
        bgPanel.add(electronicsButton);
        bgPanel.add(othersButton);

        // --- 5 REUSABLE LISTING CARD PANELS ACROSS THE ROW ---
        int startX = 40;
        int cardY = 268;
        int cardW = 204;
        int cardH = 311;
        int gapX = 41;

        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            ListingCardPanel card = new ListingCardPanel(cardW, cardH);
            card.setLocation(startX + i * (cardW + gapX), cardY);
            bgPanel.add(card);
            listingCards.add(card);
        }

        // --- PAGINATION OVERLAYS (<<, 1..5, >>) ---
        prevPageButton = createTransparentButton();
        prevPageButton.setBounds(410, 636, 42, 34);
        bgPanel.add(prevPageButton);

        int pageStartX = 442;
        int pageW = 73;
        for (int p = 0; p < 5; p++) {
            JButton pBtn = createTransparentButton();
            pBtn.setText(String.valueOf(p + 1));
            pBtn.setFont(resolveUiFont(Font.PLAIN, 16));
            pBtn.setForeground(new Color(120, 120, 120));
            pBtn.setBounds(pageStartX + p * pageW, 615, pageW, 34); 
            pageButtons.add(pBtn);
            bgPanel.add(pBtn);
        }
        
        nextPageButton = createTransparentButton();
        nextPageButton.setBounds(762, 636, 42, 34);
        bgPanel.add(nextPageButton);
    }

    public void showListingDetailsPopup(Component anchor, Listing item) {
        if (item == null) return;

        JPopupMenu detailsPopup = new JPopupMenu();
        detailsPopup.setBackground(Color.WHITE);
        detailsPopup.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        Font base = resolveUiFont(Font.PLAIN, 12);
        String family = base.getFamily().replace("'", "");

        String html = "<html><div style='width:220px; padding:6px; font-family:\"" + family + "\",SansSerif; color:#262626;'>"
            + "<div style='font-size:13px; font-weight:bold; margin-bottom:2px;'>" + escapeHtml(item.getTitle()) + "</div>"
            + "<div style='font-size:11px; color:#777777; margin-bottom:4px;'>" + escapeHtml(item.getCategory()) + " • " + escapeHtml(item.getStatus()) + "</div>"
            + "<div style='font-size:13px; font-weight:bold; margin-bottom:6px;'>PHP " + String.format("%,.0f", item.getPrice()) + "</div>"
            + "<div style='font-size:11px; margin-bottom:3px;'><b>Condition:</b> " + escapeHtml(item.getCondition()) + "</div>"
            + "<div style='font-size:11px;'><b>Description:</b> " + escapeHtml(item.getDescription()) + "</div>"
            + "</div></html>";

        JLabel infoLabel = new JLabel(html);
        infoLabel.setFont(base);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        infoLabel.setForeground(new Color(38, 38, 38));
        detailsPopup.add(infoLabel);

        detailsPopup.show(anchor, 0, anchor.getHeight());
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public void renderListings(List<Listing> pageItems, int currentPage, int totalPages) {
        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            ListingCardPanel card = listingCards.get(i);
            if (i < pageItems.size()) {
                Listing item = pageItems.get(i);
                ImageIcon scaledIcon = loadScaledIcon(
                    item.getImagePath(),
                    card.imageLabel.getWidth(),
                    card.imageLabel.getHeight()
                );
                card.bindListing(item, scaledIcon);
                card.setVisible(true);
            } else {
                card.setVisible(false);
            }
        }

        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);

        for (int p = 0; p < pageButtons.size(); p++) {
            int pageNum = p + 1;
            JButton btn = pageButtons.get(p);
            btn.setText(String.valueOf(pageNum));
            btn.setEnabled(pageNum <= totalPages);
            if (pageNum == currentPage) {
                btn.setFont(resolveUiFont(Font.BOLD, 15));
                btn.setForeground(new Color(166, 28, 42));
            } else {
                btn.setFont(resolveUiFont(Font.PLAIN, 14));
                btn.setForeground(new Color(120, 120, 120));
            }
        }

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        if (path == null || path.trim().isEmpty() || w <= 0 || h <= 0) {
            return null;
        }
        ImageIcon raw = new ImageIcon(path);
        if (raw.getIconWidth() <= 0) {
            String normalized = path.startsWith("/") ? path : "/" + path;
            URL url = getClass().getResource(normalized);
            if (url != null) {
                raw = new ImageIcon(url);
            }
        }
        if (raw.getIconWidth() <= 0) {
            return null;
        }
        Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JButton createTransparentButton() {
        JButton button = new JButton();
        button.setUI(new BasicButtonUI());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public void togglePriceFilterMenu(String[] options, java.util.function.Consumer<String> onSelect) { 
        if (pricePopupMenu != null && pricePopupMenu.isShowing()) {
            pricePopupMenu.setVisible(false);
            lastPopupHideTime = System.currentTimeMillis();
            return;
        }

        if (System.currentTimeMillis() - lastPopupHideTime < 180) {
            lastPopupHideTime = 0;
            return;
        }

        pricePopupMenu = new JPopupMenu();
        pricePopupMenu.setBackground(Color.WHITE);
        pricePopupMenu.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        pricePopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                lastPopupHideTime = System.currentTimeMillis();
            }

            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
                lastPopupHideTime = System.currentTimeMillis();
            }
        });

        for (String option : options) {
            JMenuItem item = new JMenuItem(option);
            item.setFont(resolveUiFont(Font.PLAIN, 13));
            item.setForeground(new Color(38, 38, 38));
            item.setBackground(Color.WHITE);
            item.setOpaque(true);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            item.addActionListener(e -> onSelect.accept(option));
            pricePopupMenu.add(item);
        }

        pricePopupMenu.show(priceFilterButton, 0, priceFilterButton.getHeight());
    }

    class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            ImageIcon raw = new ImageIcon(imagePath);
            if (raw.getIconWidth() <= 0) {
                String normalized = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
                URL url = getClass().getResource(normalized);
                if (url != null) {
                    raw = new ImageIcon(url);
                }
            }
            backgroundImage = raw.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}