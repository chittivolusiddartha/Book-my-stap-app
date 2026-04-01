import java.util.HashMap;

/**
 * UseCase4RoomSearch demonstrates read-only room search and availability check.
 * Guests can view available rooms without modifying system state.
 *
 * @author Your Name
 * @version 4.0
 */
public class UseCase4RoomSearch {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v4.0");
        System.out.println("========================================");

        // Initialize inventory
        RoomInventoryUC4 inventory = new RoomInventoryUC4();

        // Initialize room domain objects
        HashMap<String, RoomUC4> roomDetails = new HashMap<>();
        roomDetails.put("Single Room", new SingleRoomUC4());
        roomDetails.put("Double Room", new DoubleRoomUC4());
        roomDetails.put("Suite Room", new SuiteRoomUC4());

        // Perform search
        SearchServiceUC4 searchService = new SearchServiceUC4(inventory, roomDetails);

        System.out.println("\n--- Available Rooms ---");
        searchService.displayAvailableRooms();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * Abstract class representing a general hotel room.
 */
abstract class RoomUC4 {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected double roomSizeSqFt;

    /**
     * Constructor to initialize common room attributes.
     *
     * @param roomType      type of the room
     * @param numberOfBeds  number of beds in the room
     * @param pricePerNight price per night in USD
     * @param roomSizeSqFt  size of the room in square feet
     */
    public RoomUC4(String roomType, int numberOfBeds, double pricePerNight, double roomSizeSqFt) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSizeSqFt = roomSizeSqFt;
    }

    /**
     * Displays room details to the console.
     */
    public void displayRoomDetails() {
        System.out.println("  Room Type     : " + roomType);
        System.out.println("  Number of Beds: " + numberOfBeds);
        System.out.println("  Price/Night   : $" + pricePerNight);
        System.out.println("  Room Size     : " + roomSizeSqFt + " sq ft");
    }
}

/**
 * Concrete class representing a Single Room.
 */
class SingleRoomUC4 extends RoomUC4 {
    /** Constructor for SingleRoomUC4 with predefined attributes. */
    public SingleRoomUC4() {
        super("Single Room", 1, 80.00, 200.0);
    }
}

/**
 * Concrete class representing a Double Room.
 */
class DoubleRoomUC4 extends RoomUC4 {
    /** Constructor for DoubleRoomUC4 with predefined attributes. */
    public DoubleRoomUC4() {
        super("Double Room", 2, 120.00, 350.0);
    }
}

/**
 * Concrete class representing a Suite Room.
 */
class SuiteRoomUC4 extends RoomUC4 {
    /** Constructor for SuiteRoomUC4 with predefined attributes. */
    public SuiteRoomUC4() {
        super("Suite Room", 3, 250.00, 600.0);
    }
}

/**
 * RoomInventoryUC4 manages centralized room availability using a HashMap.
 */
class RoomInventoryUC4 {

    /** Centralized map storing room type to available count. */
    private HashMap<String, Integer> inventory;

    /**
     * Constructor that initializes room inventory with default availability.
     */
    public RoomInventoryUC4() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0);
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
     * Returns all room types in the inventory.
     *
     * @return set of room type keys
     */
    public java.util.Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

/**
 * SearchServiceUC4 provides read-only search operations on the inventory.
 * Ensures no modification of inventory state during search.
 */
class SearchServiceUC4 {

    private RoomInventoryUC4 inventory;
    private HashMap<String, RoomUC4> roomDetails;

    /**
     * Constructor to initialize the search service with inventory and room details.
     *
     * @param inventory   the centralized room inventory
     * @param roomDetails map of room type to room domain objects
     */
    public SearchServiceUC4(RoomInventoryUC4 inventory, HashMap<String, RoomUC4> roomDetails) {
        this.inventory = inventory;
        this.roomDetails = roomDetails;
    }

    /**
     * Displays only available rooms with their details and pricing.
     * Filters out room types with zero availability.
     */
    public void displayAvailableRooms() {
        boolean anyAvailable = false;

        for (String roomType : inventory.getRoomTypes()) {
            int availability = inventory.getAvailability(roomType);

            if (availability > 0) {
                anyAvailable = true;
                System.out.println("\n[" + roomType + "]");
                if (roomDetails.containsKey(roomType)) {
                    roomDetails.get(roomType).displayRoomDetails();
                }
                System.out.println("  Available Rooms: " + availability);
            }
        }

        if (!anyAvailable) {
            System.out.println("No rooms are currently available.");
        }
    }
}
