import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * UseCase6RoomAllocationService demonstrates reservation confirmation and room allocation.
 * Uses a Set to enforce unique room ID assignment and prevent double-booking.
 *
 * @author Your Name
 * @version 6.0
 */
public class UseCase6RoomAllocationService {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v6.0");
        System.out.println("========================================");

        // Initialize inventory
        InventoryServiceUC6 inventoryService = new InventoryServiceUC6();

        // Initialize booking queue and add requests
        Queue<ReservationUC6> bookingQueue = new LinkedList<>();
        bookingQueue.offer(new ReservationUC6("G001", "Alice", "Single Room"));
        bookingQueue.offer(new ReservationUC6("G002", "Bob", "Suite Room"));
        bookingQueue.offer(new ReservationUC6("G003", "Charlie", "Double Room"));
        bookingQueue.offer(new ReservationUC6("G004", "Diana", "Single Room"));
        bookingQueue.offer(new ReservationUC6("G005", "Eve", "Single Room"));

        System.out.println("\n--- Processing Booking Requests ---");

        // Initialize booking service and process queue
        BookingServiceUC6 bookingService = new BookingServiceUC6(inventoryService);
        bookingService.processQueue(bookingQueue);

        System.out.println("\n--- Final Inventory State ---");
        inventoryService.displayInventory();

        System.out.println("\n--- Allocated Room IDs ---");
        bookingService.displayAllocatedRooms();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * ReservationUC6 represents a guest's booking request.
 */
class ReservationUC6 {

    private String guestId;
    private String guestName;
    private String roomType;

    /**
     * Constructor to initialize a reservation.
     *
     * @param guestId   unique identifier for the guest
     * @param guestName name of the guest
     * @param roomType  type of room requested
     */
    public ReservationUC6(String guestId, String guestName, String roomType) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /**
     * Returns the guest ID.
     * @return guestId
     */
    public String getGuestId() { return guestId; }

    /**
     * Returns the guest name.
     * @return guestName
     */
    public String getGuestName() { return guestName; }

    /**
     * Returns the requested room type.
     * @return roomType
     */
    public String getRoomType() { return roomType; }

    /**
     * Returns a string representation of the reservation.
     * @return formatted reservation details
     */
    @Override
    public String toString() {
        return "Guest ID: " + guestId + " | Name: " + guestName + " | Room: " + roomType;
    }
}

/**
 * InventoryServiceUC6 manages centralized room availability.
 */
class InventoryServiceUC6 {

    /** Centralized map storing room type to available count. */
    private HashMap<String, Integer> inventory;

    /**
     * Constructor that initializes room inventory with default availability.
     */
    public InventoryServiceUC6() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    /**
     * Retrieves the current availability for a given room type.
     *
     * @param roomType the type of room to check
     * @return number of available rooms, or 0 if not found
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Decrements availability for the given room type by one.
     *
     * @param roomType the type of room to decrement
     */
    public void decrementAvailability(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    /**
     * Displays the current inventory state for all room types.
     */
    public void displayInventory() {
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType) + " available");
        }
    }
}

/**
 * BookingServiceUC6 processes queued booking requests and allocates rooms.
 * Uses a Set to enforce unique room ID assignment and prevent double-booking.
 */
class BookingServiceUC6 {

    /** Tracks allocated room IDs grouped by room type. */
    private HashMap<String, Set<String>> allocatedRooms;

    /** Counter map for generating unique room IDs per room type. */
    private HashMap<String, Integer> roomCounters;

    private InventoryServiceUC6 inventoryService;

    /**
     * Constructor to initialize the booking service.
     *
     * @param inventoryService the inventory service for availability management
     */
    public BookingServiceUC6(InventoryServiceUC6 inventoryService) {
        this.inventoryService = inventoryService;
        this.allocatedRooms = new HashMap<>();
        this.roomCounters = new HashMap<>();
    }

    /**
     * Processes all booking requests from the queue in FIFO order.
     *
     * @param bookingQueue the queue of pending reservations
     */
    public void processQueue(Queue<ReservationUC6> bookingQueue) {
        while (!bookingQueue.isEmpty()) {
            ReservationUC6 reservation = bookingQueue.poll();
            allocateRoom(reservation);
        }
    }

    /**
     * Allocates a room for a reservation if availability exists.
     * Generates a unique room ID and updates inventory atomically.
     *
     * @param reservation the reservation to process
     */
    private void allocateRoom(ReservationUC6 reservation) {
        String roomType = reservation.getRoomType();

        if (inventoryService.getAvailability(roomType) > 0) {
            // Generate unique room ID
            roomCounters.put(roomType, roomCounters.getOrDefault(roomType, 0) + 1);
            String roomId = roomType.replace(" ", "") + "-" + roomCounters.get(roomType);

            // Initialize set for room type if not present
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());

            // Enforce uniqueness via Set
            if (!allocatedRooms.get(roomType).contains(roomId)) {
                allocatedRooms.get(roomType).add(roomId);
                inventoryService.decrementAvailability(roomType);
                System.out.println("CONFIRMED | " + reservation + " | Room ID: " + roomId);
            }
        } else {
            System.out.println("REJECTED  | " + reservation + " | Reason: No availability");
        }
    }

    /**
     * Displays all allocated room IDs grouped by room type.
     */
    public void displayAllocatedRooms() {
        if (allocatedRooms.isEmpty()) {
            System.out.println("No rooms have been allocated.");
            return;
        }
        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + " : " + allocatedRooms.get(roomType));
        }
    }
}
