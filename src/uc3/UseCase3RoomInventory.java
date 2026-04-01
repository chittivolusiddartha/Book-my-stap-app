import java.util.HashMap;

/**
 * UseCase3RoomInventory demonstrates centralized room inventory management
 * using a HashMap to store and manage room availability.
 * Replaces scattered variables with a single source of truth.
 *
 * @author Your Name
 * @version 3.0
 */
public class UseCase3RoomInventory {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v3.0");
        System.out.println("========================================");

        RoomInventory inventory = new RoomInventory();

        System.out.println("\n--- Initial Room Inventory ---");
        inventory.displayInventory();

        System.out.println("\n--- Updating Inventory ---");
        inventory.updateAvailability("Single Room", 3);
        inventory.updateAvailability("Double Room", 0);
        System.out.println("Updated Single Room availability to 3.");
        System.out.println("Updated Double Room availability to 0.");

        System.out.println("\n--- Updated Room Inventory ---");
        inventory.displayInventory();

        System.out.println("\n--- Availability Check ---");
        System.out.println("Single Room available: " + inventory.getAvailability("Single Room"));
        System.out.println("Suite Room available : " + inventory.getAvailability("Suite Room"));

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * RoomInventory manages centralized room availability
 * using a HashMap as the single source of truth.
 */
class RoomInventory {

    /** Centralized map storing room type to available count. */
    private HashMap<String, Integer> inventory;

    /**
     * Constructor that initializes room inventory with default availability.
     */
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    /**
     * Retrieves the current availability for a given room type.
     *
     * @param roomType the type of room to check
     * @return number of available rooms, or -1 if room type not found
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    /**
     * Updates the availability count for a given room type.
     *
     * @param roomType the type of room to update
     * @param count    the new availability count
     */
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found: " + roomType);
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
