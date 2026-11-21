package umarketplace.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ListingFileStore {
    private final Path filePath;

    public ListingFileStore(String filePathStr) {
        this.filePath = Paths.get(filePathStr);
    }

    public List<Listing> loadOrSeedListings() {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                List<Listing> seeded = createSampleListings();
                saveListings(seeded);
                return seeded;
            }
            List<Listing> loaded = readListings();
            if (loaded.isEmpty()) {
                List<Listing> seeded = createSampleListings();
                saveListings(seeded);
                return seeded;
            }
            return loaded;
        } catch (IOException e) {
            return createSampleListings();
        }
    }

    public void saveListings(List<Listing> listings) {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (Listing listing : listings) {
                    writer.write(listing.toFileLine());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Listing> readListings() throws IOException {
        List<Listing> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Listing item = Listing.fromFileLine(line);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    private List<Listing> createSampleListings() { //SEEDED LISTINGS
        List<Listing> items = new ArrayList<>();
        // Page 1 sample listings
        items.add(new Listing("L001", "Uniforms", "PE uniform set", "RESERVED", 350.0,
            "pe.jpg", "0917-111-0001", "Like New", "Complete PE shirt and jogging pants, medium size."));
        items.add(new Listing("L002", "School Supplies", "sketchpad", "AVAILABLE", 100.0,
            "sketchpad.jpg", "0917-111-0002", "Brand New", "Spiral drawing pad with thick acid-free sheets."));
        items.add(new Listing("L003", "Electronics", "ASUS laptop", "RESERVED", 6500.0,
            "asus.jpg", "0917-111-0003", "Good", "Runs smoothly for coursework, charger included."));
        items.add(new Listing("L004", "Electronics", "Ethernet cable", "AVAILABLE", 250.0,
            "utp.jpg", "0917-111-0004", "Like New", "Cat6 LAN cable, tested working for lab/dorm setup."));
        items.add(new Listing("L005", "Electronics", "Sci cal", "SOLD", 450.0,
            "scical.jpg", "0917-111-0005", "Good", "Scientific calculator with slide cover, all keys intact."));

        // Page 2 sample listings
        items.add(new Listing("L006", "Books", "1984", "AVAILABLE", 220.0,
            "1984.jpg", "0917-111-0006", "Good", "1984 by George Orwell"));
        items.add(new Listing("L007", "School Supplies", "Gel Pen Pack", "AVAILABLE", 85.0,
            "", "0917-111-0007", "Brand New", "Black 0.5mm pens, pack of 6."));
        items.add(new Listing("L008", "Others", "UM tumbler", "AVAILABLE", 300.0,
            "tumbler.jpg", "0917-111-0008", "Like New", "Official UM tumbler merch"));
        items.add(new Listing("L009", "Electronics", "Wireless Mouse", "AVAILABLE", 380.0,
            "", "0917-111-0009", "Like New", "2.4GHz USB receiver included, works for laptop/PC."));
        return items;
    }
}