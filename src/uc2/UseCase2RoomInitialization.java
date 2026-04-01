/**
 * UseCase2RoomInitialization demonstrates basic room type modeling
 * using abstract classes, inheritance, and polymorphism.
 * Room availability is stored using simple variables.
 *
 * @author Your Name
 * @version 2.0
 */

/**
 * Abstract class representing a general hotel room.
 */
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected double roomSizeSqFt;

    /**
     * Constructor to initialize common room attributes.
     *
     * @param roomType       type of the room
     * @param numberOfBeds   number of beds in the room
     * @param pricePerNight  price per night in USD
     * @param roomSizeSqFt   size of the room in square feet
     */
    public Room(String roomType, int numberOfBeds, double pricePerNight, double roomSizeSqFt) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSizeSqFt = roomSizeSqFt;
    }

    /**
     * Abstract method to display room details.
     * Must be implemented by all concrete room classes.
     */
    public abstract void displayRoomDetails();
}

/**
 * Concrete class representing a Single Room.
 */
class SingleRoom extends Room {

    /**
     * Constructor for SingleRoom with predefined attributes.
     */
    public SingleRoom() {
        super("Single Room", 1, 80.00, 200.0);
    }

    /**
     * Displays details specific to a Single Room.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Price/Night   : $" + pricePerNight);
        System.out.println("Room Size     : " + roomSizeSqFt + " sq ft");
    }
}

/**
 * Concrete class representing a Double Room.
 */
class DoubleRoom extends Room {

    /**
     * Constructor for DoubleRoom with predefined attributes.
     */
    public DoubleRoom() {
        super("Double Room", 2, 120.00, 350.0);
    }

    /**
     * Displays details specific to a Double Room.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Price/Night   : $" + pricePerNight);
        System.out.println("Room Size     : " + roomSizeSqFt + " sq ft");
    }
}

/**
 * Concrete class representing a Suite Room.
 */
class SuiteRoom extends Room {

    /**
     * Constructor for SuiteRoom with predefined attributes.
     */
    public SuiteRoom() {
        super("Suite Room", 3, 250.00, 600.0);
    }

    /**
     * Displays details specific to a Suite Room.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type     : " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Price/Night   : $" + pricePerNight);
        System.out.println("Room Size     : " + roomSizeSqFt + " sq ft");
    }
}

/**
 * Main application class for Use Case 2.
 * Initializes room objects and displays availability using simple variables.
 */
public class UseCase2RoomInitialization {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Static availability variables
        boolean isSingleRoomAvailable = true;
        boolean isDoubleRoomAvailable = false;
        boolean isSuiteRoomAvailable = true;

        // Create room objects using polymorphism
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        System.out.println("========================================");
        System.out.println("   Book My Stay App - Room Details");
        System.out.println("   Hotel Booking System v2.0");
        System.out.println("========================================");

        System.out.println("\n--- Single Room ---");
        singleRoom.displayRoomDetails();
        System.out.println("Available     : " + isSingleRoomAvailable);

        System.out.println("\n--- Double Room ---");
        doubleRoom.displayRoomDetails();
        System.out.println("Available     : " + isDoubleRoomAvailable);

        System.out.println("\n--- Suite Room ---");
        suiteRoom.displayRoomDetails();
        System.out.println("Available     : " + isSuiteRoomAvailable);

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}
