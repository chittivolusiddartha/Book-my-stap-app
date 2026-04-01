import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase5BookingRequestQueue demonstrates fair booking request handling
 * using a Queue data structure based on the FIFO principle.
 * No inventory mutation occurs at this stage.
 *
 * @author Your Name
 * @version 5.0
 */
public class UseCase5BookingRequestQueue {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v5.0");
        System.out.println("========================================");

        // Initialize the booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("G001", "Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("G002", "Bob", "Suite Room"));
        bookingQueue.addRequest(new Reservation("G003", "Charlie", "Double Room"));
        bookingQueue.addRequest(new Reservation("G004", "Diana", "Single Room"));
        bookingQueue.addRequest(new Reservation("G005", "Eve", "Suite Room"));

        System.out.println("\n--- Booking Requests Received ---");
        bookingQueue.displayQueue();

        System.out.println("\n--- Processing Requests (FIFO) ---");
        bookingQueue.processRequests();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * Reservation represents a guest's intent to book a specific room type.
 */
class Reservation {

    private String guestId;
    private String guestName;
    private String roomType;

    /**
     * Constructor to initialize a reservation request.
     *
     * @param guestId   unique identifier for the guest
     * @param guestName name of the guest
     * @param roomType  type of room requested
     */
    public Reservation(String guestId, String guestName, String roomType) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /**
     * Returns the guest ID.
     *
     * @return guestId
     */
    public String getGuestId() {
        return guestId;
    }

    /**
     * Returns the guest name.
     *
     * @return guestName
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Returns the requested room type.
     *
     * @return roomType
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * Returns a string representation of the reservation.
     *
     * @return formatted reservation details
     */
    @Override
    public String toString() {
        return "Guest ID: " + guestId + " | Name: " + guestName + " | Room: " + roomType;
    }
}

/**
 * BookingRequestQueue manages incoming booking requests using a Queue.
 * Ensures requests are processed in the order they are received (FIFO).
 */
class BookingRequestQueue {

    /** Queue to store booking requests in arrival order. */
    private Queue<Reservation> requestQueue;

    /**
     * Constructor to initialize the booking request queue.
     */
    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Adds a new booking request to the queue.
     *
     * @param reservation the reservation request to add
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    /**
     * Displays all pending requests in the queue without modifying it.
     */
    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No pending booking requests.");
            return;
        }
        int position = 1;
        for (Reservation r : requestQueue) {
            System.out.println("Position " + position++ + ": " + r);
        }
    }

    /**
     * Processes all requests in FIFO order.
     * Simulates handing off requests to the allocation system.
     */
    public void processRequests() {
        if (requestQueue.isEmpty()) {
            System.out.println("No requests to process.");
            return;
        }
        while (!requestQueue.isEmpty()) {
            Reservation r = requestQueue.poll();
            System.out.println("Processing: " + r);
        }
        System.out.println("\nAll requests have been forwarded for allocation.");
    }
}
