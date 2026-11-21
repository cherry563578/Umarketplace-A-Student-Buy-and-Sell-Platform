package umarketplace.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import umarketplace.model.Listing;
import umarketplace.model.ListingFileStore;
import umarketplace.view.HomepageView;
import umarketplace.view.ListingCardPanel;

public class HomepageController {
    private final HomepageView view;
    private final ListingFileStore store;

    private List<Listing> allListings = new ArrayList<>();
    private List<Listing> filteredListings = new ArrayList<>();

    private String selectedCategory = "ALL";
    private String selectedPriceBracket = "ALL";
    private String searchQuery = "";
    private int currentPage = 1;

    public HomepageController(HomepageView view, ListingFileStore store) {
        this.view = view;
        this.store = store;

        this.allListings = this.store.loadOrSeedListings();
        this.filteredListings = new ArrayList<>(this.allListings);

        attachListeners();
        refreshView();
    }

    private void attachListeners() {
        view.searchButton.addActionListener(e -> applySearch());
        view.searchField.addActionListener(e -> applySearch());

        view.homeButton.addActionListener(e -> resetFilters());
        view.notifButton.addActionListener(e ->
            JOptionPane.showMessageDialog(view, "No new notifications.", "Notifications", JOptionPane.INFORMATION_MESSAGE)
        );
        view.profileButton.addActionListener(e ->
            JOptionPane.showMessageDialog(view, "User Profile view can be opened here.", "User Profile", JOptionPane.INFORMATION_MESSAGE)
        );
        view.helpButton.addActionListener(e ->
            JOptionPane.showMessageDialog(view, "Search items, pick a category or price filter, and click the phone icon to contact sellers offline.", "Help", JOptionPane.INFORMATION_MESSAGE)
        );

        view.booksButton.addActionListener(e -> toggleCategory("Books"));
        view.uniformsButton.addActionListener(e -> toggleCategory("Uniforms"));
        view.schoolSuppliesButton.addActionListener(e -> toggleCategory("School Supplies"));
        view.electronicsButton.addActionListener(e -> toggleCategory("Electronics"));
        view.othersButton.addActionListener(e -> toggleCategory("Others"));

        view.priceFilterButton.addActionListener(e -> choosePriceBracket());

        view.prevPageButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                refreshView();
            }
        });

        view.nextPageButton.addActionListener(e -> {
            int totalPages = getTotalPages();
            if (currentPage < totalPages) {
                currentPage++;
                refreshView();
            }
        });

        for (int i = 0; i < view.pageButtons.size(); i++) {
            final int targetPage = i + 1;
            view.pageButtons.get(i).addActionListener(e -> {
                if (targetPage <= getTotalPages()) {
                    currentPage = targetPage;
                    refreshView();
                }
            });
        }

        for (int i = 0; i < view.listingCards.size(); i++) {
            final int slotIndex = i;
            ListingCardPanel card = view.listingCards.get(i);

            card.contactButton.addActionListener(e -> {
                Listing item = getListingAtSlot(slotIndex);
                if (item != null) {
                    JOptionPane.showMessageDialog(
                        view,
                        "Seller Contact (" + item.getTitle() + "): " + item.getSellerContact(),
                        "Seller Contact",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            });

            card.onTitleClick(() -> {
                Listing item = getListingAtSlot(slotIndex);
                if (item != null) {
                    view.showListingDetailsPopup(card.titleLabel, item);
                }
            });

            card.onPriceClick(() -> {
                Listing item = getListingAtSlot(slotIndex);
                if (item != null) {
                    view.showListingDetailsPopup(card.priceLabel, item);
                }
            });
        }
    }

    private void applySearch() {
        searchQuery = view.searchField.getText().trim().toLowerCase();
        applyFilters();
    }

    private void toggleCategory(String category) {
        selectedCategory = selectedCategory.equalsIgnoreCase(category) ? "ALL" : category;
        applyFilters();
    }

    private void choosePriceBracket() {
        String[] options = {
            "All",
            "Under PHP 299",
            "PHP 300 - PHP 499",
            "PHP 500 - PHP 999",
            "PHP 1,000 - PHP 1,999",
            "PHP 2,000+"
        };

        view.togglePriceFilterMenu(options, choice -> {
            selectedPriceBracket = choice.equals("All") ? "ALL" : choice;
            applyFilters();
        });
    }

    private void resetFilters() {
        selectedCategory = "ALL";
        selectedPriceBracket = "ALL";
        searchQuery = "";
        view.searchField.setText("");
        applyFilters();
    }

    private void applyFilters() {
        filteredListings = new ArrayList<>();
        for (Listing item : allListings) {
            boolean matchesCategory = selectedCategory.equals("ALL")
                || item.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesSearch = searchQuery.isEmpty()
                || item.getTitle().toLowerCase().contains(searchQuery)
                || item.getCategory().toLowerCase().contains(searchQuery);
            boolean matchesPrice = matchesPriceBracket(item.getPrice(), selectedPriceBracket);

            if (matchesCategory && matchesSearch && matchesPrice) {
                filteredListings.add(item);
            }
        }
        currentPage = 1;
        refreshView();
    }

    private boolean matchesPriceBracket(double price, String bracket) {
        switch (bracket) {
            case "Under PHP 299":
                return price < 299;
            case "PHP 300 - PHP 499":
                return price >= 300 && price <= 499;
            case "PHP 500 - PHP 999":
                return price >= 500 && price <= 999;
            case "PHP 1,000 - PHP 1,999":
                return price >= 1000 && price <= 1999;
            case "PHP 2,000+":
                return price >= 2000;
            default:
                return true;
        }
    }

    private int getTotalPages() {
        if (filteredListings.isEmpty()) return 1;
        return (int) Math.ceil((double) filteredListings.size() / HomepageView.ITEMS_PER_PAGE);
    }

    private List<Listing> getPageSlice() {
        int totalPages = getTotalPages();
        if (currentPage > totalPages) currentPage = totalPages;
        int start = (currentPage - 1) * HomepageView.ITEMS_PER_PAGE;
        int end = Math.min(start + HomepageView.ITEMS_PER_PAGE, filteredListings.size());
        if (start >= filteredListings.size()) return new ArrayList<>();
        return filteredListings.subList(start, end);
    }

    private Listing getListingAtSlot(int slotIndex) {
        List<Listing> slice = getPageSlice();
        if (slotIndex >= 0 && slotIndex < slice.size()) {
            return slice.get(slotIndex);
        }
        return null;
    }

    private void refreshView() {
        view.renderListings(getPageSlice(), currentPage, getTotalPages());
    }
}