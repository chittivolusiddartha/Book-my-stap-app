import java.util.HashMap;
import java.util.Map;

/**
 * UseCase9ErrorHandlingValidation demonstrates structured validation and error handling
 * using custom exceptions to ensure system reliability and prevent invalid state changes.
 *
 * @author Your Name
 * @version 9.0
 */
public class UseCase9ErrorHandlingValidation {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v9.0");
        System.out.println("========================================");

        // Initialize inventory and validator
        InventoryServiceUC9 inventoryService = new InventoryServiceUC9();
        InvalidBookingValidator validator = new InvalidBookingValidator(inventoryService);

        System.out.println("\n--- Validation Test Cases ---");

        // Test 1: Valid booking
        processBooking(validator, "G001", "Alice", "Single Room");

        // Test 2: Invalid room type
        processBooking(validator, "G002", "Bob", "Penthouse");

        // Test 3: Empty guest name
        processBooking(validator, "G003", "", "Double Room");

        // Test 4: Null guest ID
        processBooking(validator, null, "Charlie", "Suite Room");

        // Test 5: Room with no availability
        processBooking(validator, "G004", "Diana", "Double Room");

        // Test 6: Negative availability guard
        processBooking(validator, "G005", "Eve", "Suite Room");

        System.out.println("\n--- Final Inventory State ---");
        inventoryService.displayInventory();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }

    /**
     * Processes a booking request with full validation and error handling.
     *
     * @param validator the booking validator
     * @param guestId   the guest ID
     * @param guestName the guest name
     * @param roomType  the requested room type
     */
    private static void processBooking(InvalidBookingValidator validator,
                                       String guestId, String guestName, String roomType) {
        try {
            validator.validate(guestId, guestName, roomType);
            System.out.println("SUCCESS  | Guest: " + guestName
                    + " | Room: " + roomType + " | Booking validated and confirmed.");
        } catch (InvalidBookingException e) {
            System.out.println("FAILED   | Reason: " + e.getMessage());
        }
    }
}

/**
 * InvalidBookingException is a custom exception representing invalid booking scenarios.
 */
class InvalidBookingException extends Exception {

    /**
     * Constructor to initialize with a descriptive error message.
     *
     * @param message the error message explaining the validation failure
     */
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * InventoryServiceUC9 manages centralized room availability.
 */
class InventoryServiceUC9 {

    /** Centralized map storing room type to available count. */
    private Map<String, Integer> inventory;

    /**
     * Constructor that initializes room inventory with default availability.
     */
    public InventoryServiceUC9() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room",  1);
    }

    /**
     * Returns the set of valid room types.
     *
     * @return set of room type keys
     */
    public java.util.Set<String> getValidRoomTypes() {
        return inventory.keySet();
    }

    /**
     * Retrieves the current availability for a given room type.
     *
     * @param roomType the type of room to check
     * @return number of available rooms
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
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

/**
 * InvalidBookingValidator validates booking input and system state
 * before allowing a booking to proceed. Uses fail-fast design.
 */
class InvalidBookingValidator {

    private InventoryServiceUC9 inventoryService;

    /**
     * Constructor to initialize the validator with the inventory service.
     *
     * @param inventoryService the inventory service for availability checks
     */
    public InvalidBookingValidator(InventoryServiceUC9 inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Validates a booking request against input and system constraints.
     * Throws InvalidBookingException immediately upon first failure (fail-fast).
     *
     * @param guestId   the guest ID to validate
     * @param guestName the guest name to validate
     * @param roomType  the room type to validate
     * @throws InvalidBookingException if any validation check fails
     */
    public void validate(String guestId, String guestName, String roomType)
            throws InvalidBookingException {

        // Validate guest ID
        if (guestId == null || guestId.trim().isEmpty()) {
            throw new InvalidBookingException("Guest ID must not be null or empty.");
        }

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name must not be null or empty.");
        }

        // Validate room type exists
        if (!inventoryService.getValidRoomTypes().contains(roomType)) {
            throw new InvalidBookingException(
                "Invalid room type: '" + roomType + "'. "
                + "Valid types are: " + inventoryService.getValidRoomTypes());
        }

        // Validate room availability
        int availability = inventoryService.getAvailability(roomType);
        if (availability <= 0) {
            throw new InvalidBookingException(
                "No availability for room type: '" + roomType + "'.");
        }

        // All checks passed — decrement inventory
        inventoryService.decrementAvailability(roomType);
    }
}
