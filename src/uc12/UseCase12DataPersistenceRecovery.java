import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UseCase12DataPersistenceRecovery demonstrates file-based data persistence
 * and system recovery. Booking history and inventory state are saved to files
 * and restored on application startup.
 *
 * @author Your Name
 * @version 12.0
 */
public class UseCase12DataPersistenceRecovery {

    /** File path for persisting inventory state. */
    private static final String INVENTORY_FILE     = "inventory.txt";

    /** File path for persisting booking history. */
    private static final String BOOKING_HISTORY_FILE = "booking_history.txt";

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v12.0");
        System.out.println("========================================");

        PersistenceService persistenceService = new PersistenceService(
                INVENTORY_FILE, BOOKING_HISTORY_FILE);

        // --- PHASE 1: System startup — attempt recovery ---
        System.out.println("\n--- Phase 1: System Recovery ---");
        InventoryStateUC12    inventory      = persistenceService.loadInventory();
        BookingHistoryUC12    bookingHistory = persistenceService.loadBookingHistory();

        System.out.println("\nRecovered Inventory:");
        inventory.displayInventory();

        System.out.println("\nRecovered Booking History:");
        bookingHistory.displayHistory();

        // --- PHASE 2: Simulate new bookings ---
        System.out.println("\n--- Phase 2: Simulating New Bookings ---");
        bookingHistory.addBooking(new BookingRecordUC12("G001", "Alice",   "Single Room", "SingleRoom-1"));
        bookingHistory.addBooking(new BookingRecordUC12("G002", "Bob",     "Suite Room",  "SuiteRoom-1"));
        bookingHistory.addBooking(new BookingRecordUC12("G003", "Charlie", "Double Room", "DoubleRoom-1"));

        inventory.updateAvailability("Single Room", 2);
        inventory.updateAvailability("Double Room", 1);
        inventory.updateAvailability("Suite Room",  0);

        // --- PHASE 3: Persist state before shutdown ---
        System.out.println("\n--- Phase 3: Persisting State ---");
        persistenceService.saveInventory(inventory);
        persistenceService.saveBookingHistory(bookingHistory);

        System.out.println("\nFinal Inventory:");
        inventory.displayInventory();

        System.out.println("\nFinal Booking History:");
        bookingHistory.displayHistory();

        System.out.println("\n========================================");
        System.out.println("State saved. Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * BookingRecordUC12 represents a confirmed booking record for persistence.
 */
class BookingRecordUC12 {

    private String guestId;
    private String guestName;
    private String roomType;
    private String roomId;

    /**
     * Constructor to initialize a booking record.
     *
     * @param guestId   unique identifier for the guest
     * @param guestName name of the guest
     * @param roomType  type of room booked
     * @param roomId    unique room ID assigned
     */
    public BookingRecordUC12(String guestId, String guestName,
                             String roomType, String roomId) {
        this.guestId   = guestId;
        this.guestName = guestName;
        this.roomType  = roomType;
        this.roomId    = roomId;
    }

    /** @return guestId */
    public String getGuestId()   { return guestId; }

    /** @return guestName */
    public String getGuestName() { return guestName; }

    /** @return roomType */
    public String getRoomType()  { return roomType; }

    /** @return roomId */
    public String getRoomId()    { return roomId; }

    /**
     * Serializes the booking record to a CSV line.
     *
     * @return comma-separated string representation
     */
    public String serialize() {
        return guestId + "," + guestName + "," + roomType + "," + roomId;
    }

    /**
     * Deserializes a CSV line into a BookingRecordUC12 object.
     *
     * @param line the CSV line to parse
     * @return a BookingRecordUC12 instance
     */
    public static BookingRecordUC12 deserialize(String line) {
        String[] parts = line.split(",");
        return new BookingRecordUC12(parts[0], parts[1], parts[2], parts[3]);
    }

    /**
     * Returns a formatted string representation of the booking record.
     *
     * @return formatted booking details
     */
    @Override
    public String toString() {
        return "Guest ID: " + guestId
                + " | Name: "      + guestName
                + " | Room Type: " + roomType
                + " | Room ID: "   + roomId;
    }
}

/**
 * InventoryStateUC12 manages in-memory room availability with serialization support.
 */
class InventoryStateUC12 {

    /** Centralized map storing room type to available count. */
    private Map<String, Integer> inventory;

    /**
     * Constructor that initializes a default inventory.
     */
    public InventoryStateUC12() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room",  2);
    }

    /**
     * Constructor that initializes inventory from a given map.
     *
     * @param inventory the inventory map to use
     */
    public InventoryStateUC12(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    /**
     * Updates availability for a given room type.
     *
     * @param roomType the type of room to update
     * @param count    the new availability count
     */
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Returns the inventory map.
     *
     * @return inventory map
     */
    public Map<String, Integer> getInventory() {
        return inventory;
    }

    /**
     * Displays the current inventory state.
     */
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("  No inventory data.");
            return;
        }
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

/**
 * BookingHistoryUC12 maintains an ordered list of booking records
 * with serialization support for persistence.
 */
class BookingHistoryUC12 {

    /** Ordered list of booking records. */
    private List<BookingRecordUC12> history;

    /**
     * Constructor to initialize an empty booking history.
     */
    public BookingHistoryUC12() {
        history = new ArrayList<>();
    }

    /**
     * Adds a booking record to the history.
     *
     * @param record the booking record to add
     */
    public void addBooking(BookingRecordUC12 record) {
        history.add(record);
        System.out.println("  Added: " + record);
    }

    /**
     * Returns the list of booking records.
     *
     * @return list of booking records
     */
    public List<BookingRecordUC12> getHistory() {
        return history;
    }

    /**
     * Displays all booking records in order.
     */
    public void displayHistory() {
        if (history.isEmpty()) {
            System.out.println("  No booking history.");
            return;
        }
        int index = 1;
        for (BookingRecordUC12 record : history) {
            System.out.println("  " + index++ + ". " + record);
        }
    }
}

/**
 * PersistenceService handles saving and loading inventory and booking history
 * to and from files. Handles missing or corrupted files gracefully.
 */
class PersistenceService {

    private String inventoryFile;
    private String bookingHistoryFile;

    /**
     * Constructor to initialize the persistence service with file paths.
     *
     * @param inventoryFile      path to the inventory persistence file
     * @param bookingHistoryFile path to the booking history persistence file
     */
    public PersistenceService(String inventoryFile, String bookingHistoryFile) {
        this.inventoryFile      = inventoryFile;
        this.bookingHistoryFile = bookingHistoryFile;
    }

    /**
     * Saves the inventory state to a file in key=value format.
     *
     * @param inventory the inventory state to persist
     */
    public void saveInventory(InventoryStateUC12 inventory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile))) {
            for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Inventory saved to: " + inventoryFile);
        } catch (IOException e) {
            System.out.println("ERROR: Could not save inventory. " + e.getMessage());
        }
    }

    /**
     * Loads inventory state from file. Returns default inventory if file is missing.
     *
     * @return the restored InventoryStateUC12 instance
     */
    public InventoryStateUC12 loadInventory() {
        Map<String, Integer> inventoryMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inventoryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    inventoryMap.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                }
            }
            System.out.println("Inventory loaded from: " + inventoryFile);
            return new InventoryStateUC12(inventoryMap);
        } catch (IOException e) {
            System.out.println("No saved inventory found. Starting with defaults.");
            return new InventoryStateUC12();
        }
    }

    /**
     * Saves booking history to a file in CSV format.
     *
     * @param bookingHistory the booking history to persist
     */
    public void saveBookingHistory(BookingHistoryUC12 bookingHistory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookingHistoryFile))) {
            for (BookingRecordUC12 record : bookingHistory.getHistory()) {
                writer.write(record.serialize());
                writer.newLine();
            }
            System.out.println("Booking history saved to: " + bookingHistoryFile);
        } catch (IOException e) {
            System.out.println("ERROR: Could not save booking history. " + e.getMessage());
        }
    }

    /**
     * Loads booking history from file. Returns empty history if file is missing.
     *
     * @return the restored BookingHistoryUC12 instance
     */
    public BookingHistoryUC12 loadBookingHistory() {
        BookingHistoryUC12 bookingHistory = new BookingHistoryUC12();
        try (BufferedReader reader = new BufferedReader(new FileReader(bookingHistoryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    bookingHistory.getHistory().add(BookingRecordUC12.deserialize(line));
                }
            }
            System.out.println("Booking history loaded from: " + bookingHistoryFile);
        } catch (IOException e) {
            System.out.println("No saved booking history found. Starting fresh.");
        }
        return bookingHistory;
    }
}
