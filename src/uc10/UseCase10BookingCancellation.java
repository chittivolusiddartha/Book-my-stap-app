import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * UseCase10BookingCancellation demonstrates safe booking cancellation
 * with inventory rollback using a Stack data structure (LIFO).
 * System state is restored consistently after each cancellation.
 *
 * @author Your Name
 * @version 10.0
 */
public class UseCase10BookingCancellation {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v10.0");
        System.out.println("========================================");

        // Initialize inventory
        InventoryServiceUC10 inventoryService = new InventoryServiceUC10();

        // Initialize booking history with confirmed bookings
        BookingHistoryUC10 bookingHistory = new BookingHistoryUC10();
        bookingHistory.addBooking(new ConfirmedBookingUC10("G001", "Alice",   "Single Room", "SingleRoom-1"));
        bookingHistory.addBooking(new ConfirmedBookingUC10("G002", "Bob",     "Suite Room",  "SuiteRoom-1"));
        bookingHistory.addBooking(new ConfirmedBookingUC10("G003", "Charlie", "Double Room", "DoubleRoom-1"));
        bookingHistory.addBooking(new ConfirmedBookingUC10("G004", "Diana",   "Single Room", "SingleRoom-2"));

        System.out.println("\n--- Initial Inventory ---");
        inventoryService.displayInventory();

        // Initialize cancellation service
        CancellationService cancellationService = new CancellationService(
                inventoryService, bookingHistory);

        System.out.println("\n--- Processing Cancellations ---");

        // Test 1: Valid cancellation
        cancellationService.cancelBooking("G002");

        // Test 2: Valid cancellation
        cancellationService.cancelBooking("G004");

        // Test 3: Already cancelled booking
        cancellationService.cancelBooking("G002");

        // Test 4: Non-existent booking
        cancellationService.cancelBooking("G999");

        System.out.println("\n--- Rollback History (LIFO Stack) ---");
        cancellationService.displayRollbackStack();

        System.out.println("\n--- Updated Inventory After Cancellations ---");
        inventoryService.displayInventory();

        System.out.println("\n--- Remaining Active Bookings ---");
        bookingHistory.displayActiveBookings();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * ConfirmedBookingUC10 represents a successfully confirmed reservation record.
 */
class ConfirmedBookingUC10 {

    private String guestId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean cancelled;

    /**
     * Constructor to initialize a confirmed booking.
     *
     * @param guestId   unique identifier for the guest
     * @param guestName name of the guest
     * @param roomType  type of room booked
     * @param roomId    unique room ID assigned
     */
    public ConfirmedBookingUC10(String guestId, String guestName,
                                String roomType, String roomId) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.cancelled = false;
    }

    /** @return guestId */
    public String getGuestId()   { return guestId; }

    /** @return guestName */
    public String getGuestName() { return guestName; }

    /** @return roomType */
    public String getRoomType()  { return roomType; }

    /** @return roomId */
    public String getRoomId()    { return roomId; }

    /** @return true if booking is cancelled */
    public boolean isCancelled() { return cancelled; }

    /** Marks the booking as cancelled. */
    public void cancel()         { this.cancelled = true; }

    /**
     * Returns a string representation of the confirmed booking.
     *
     * @return formatted booking details
     */
    @Override
    public String toString() {
        return "Guest ID: " + guestId
                + " | Name: "      + guestName
                + " | Room Type: " + roomType
                + " | Room ID: "   + roomId
                + " | Status: "    + (cancelled ? "CANCELLED" : "ACTIVE");
    }
}

/**
 * InventoryServiceUC10 manages centralized room availability.
 */
class InventoryServiceUC10 {

    /** Centralized map storing room type to available count. */
    private Map<String, Integer> inventory;

    /**
     * Constructor that initializes room inventory with default availability.
     */
    public InventoryServiceUC10() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room",  0);
    }

    /**
     * Increments availability for a given room type by one (rollback).
     *
     * @param roomType the type of room to restore
     */
    public void incrementAvailability(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    /**
     * Displays the current inventory state for all room types.
     */
    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

/**
 * BookingHistoryUC10 maintains a chronological list of confirmed bookings.
 */
class BookingHistoryUC10 {

    /** Ordered list of all bookings including cancelled ones. */
    private List<ConfirmedBookingUC10> history;

    /**
     * Constructor to initialize an empty booking history.
     */
    public BookingHistoryUC10() {
        history = new ArrayList<>();
    }

    /**
     * Adds a confirmed booking to the history.
     *
     * @param booking the confirmed booking to record
     */
    public void addBooking(ConfirmedBookingUC10 booking) {
        history.add(booking);
        System.out.println("Recorded : " + booking);
    }

    /**
     * Finds a booking by guest ID.
     *
     * @param guestId the guest ID to search for
     * @return the matching booking or null if not found
     */
    public ConfirmedBookingUC10 findByGuestId(String guestId) {
        for (ConfirmedBookingUC10 booking : history) {
            if (booking.getGuestId().equals(guestId)) {
                return booking;
            }
        }
        return null;
    }

    /**
     * Displays all active (non-cancelled) bookings.
     */
    public void displayActiveBookings() {
        boolean any = false;
        for (ConfirmedBookingUC10 booking : history) {
            if (!booking.isCancelled()) {
                System.out.println(booking);
                any = true;
            }
        }
        if (!any) {
            System.out.println("No active bookings.");
        }
    }
}

/**
 * CancellationService handles booking cancellations and inventory rollback.
 * Uses a Stack to track released room IDs in LIFO order.
 */
class CancellationService {

    private InventoryServiceUC10 inventoryService;
    private BookingHistoryUC10   bookingHistory;

    /** Stack to track released room IDs for rollback (LIFO). */
    private Stack<String> rollbackStack;

    /**
     * Constructor to initialize the cancellation service.
     *
     * @param inventoryService the inventory service for rollback updates
     * @param bookingHistory   the booking history to update on cancellation
     */
    public CancellationService(InventoryServiceUC10 inventoryService,
                               BookingHistoryUC10 bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory   = bookingHistory;
        this.rollbackStack    = new Stack<>();
    }

    /**
     * Cancels a booking for the given guest ID and performs inventory rollback.
     *
     * @param guestId the ID of the guest requesting cancellation
     */
    public void cancelBooking(String guestId) {
        ConfirmedBookingUC10 booking = bookingHistory.findByGuestId(guestId);

        if (booking == null) {
            System.out.println("FAILED   | Guest ID: " + guestId
                    + " | Reason: Booking not found.");
            return;
        }

        if (booking.isCancelled()) {
            System.out.println("FAILED   | Guest ID: " + guestId
                    + " | Reason: Booking already cancelled.");
            return;
        }

        // Perform rollback — mark cancelled, restore inventory, push to stack
        booking.cancel();
        inventoryService.incrementAvailability(booking.getRoomType());
        rollbackStack.push(booking.getRoomId());

        System.out.println("CANCELLED | Guest: " + booking.getGuestName()
                + " | Room ID: " + booking.getRoomId()
                + " | Inventory restored for: " + booking.getRoomType());
    }

    /**
     * Displays the rollback stack of released room IDs in LIFO order.
     */
    public void displayRollbackStack() {
        if (rollbackStack.isEmpty()) {
            System.out.println("No rollbacks recorded.");
            return;
        }
        System.out.println("Released Room IDs (most recent first): " + rollbackStack);
    }
}
