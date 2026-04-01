import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * UseCase11ConcurrentBookingSimulation demonstrates thread-safe booking
 * processing using synchronization to prevent race conditions and
 * double allocation under concurrent multi-user conditions.
 *
 * @author Your Name
 * @version 11.0
 */
public class UseCase11ConcurrentBookingSimulation {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v11.0");
        System.out.println("========================================");

        // Shared inventory and queue
        SharedInventory sharedInventory = new SharedInventory();
        SharedBookingQueue sharedQueue  = new SharedBookingQueue();

        // Add booking requests to shared queue
        sharedQueue.enqueue(new BookingRequestUC11("G001", "Alice",   "Single Room"));
        sharedQueue.enqueue(new BookingRequestUC11("G002", "Bob",     "Single Room"));
        sharedQueue.enqueue(new BookingRequestUC11("G003", "Charlie", "Suite Room"));
        sharedQueue.enqueue(new BookingRequestUC11("G004", "Diana",   "Double Room"));
        sharedQueue.enqueue(new BookingRequestUC11("G005", "Eve",     "Single Room"));
        sharedQueue.enqueue(new BookingRequestUC11("G006", "Frank",   "Suite Room"));
        sharedQueue.enqueue(new BookingRequestUC11("G007", "Grace",   "Double Room"));

        System.out.println("\n--- Launching Concurrent Booking Threads ---");

        // Create and start multiple threads simulating concurrent guests
        Thread t1 = new Thread(new ConcurrentBookingProcessor(sharedQueue, sharedInventory), "Thread-1");
        Thread t2 = new Thread(new ConcurrentBookingProcessor(sharedQueue, sharedInventory), "Thread-2");
        Thread t3 = new Thread(new ConcurrentBookingProcessor(sharedQueue, sharedInventory), "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();

        System.out.println("\n--- Final Inventory State ---");
        sharedInventory.displayInventory();

        System.out.println("\n--- Allocated Room IDs ---");
        sharedInventory.displayAllocatedRooms();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * BookingRequestUC11 represents a guest's concurrent booking request.
 */
class BookingRequestUC11 {

    private String guestId;
    private String guestName;
    private String roomType;

    /**
     * Constructor to initialize a booking request.
     *
     * @param guestId   unique identifier for the guest
     * @param guestName name of the guest
     * @param roomType  type of room requested
     */
    public BookingRequestUC11(String guestId, String guestName, String roomType) {
        this.guestId   = guestId;
        this.guestName = guestName;
        this.roomType  = roomType;
    }

    /** @return guestId */
    public String getGuestId()   { return guestId; }

    /** @return guestName */
    public String getGuestName() { return guestName; }

    /** @return roomType */
    public String getRoomType()  { return roomType; }

    /**
     * Returns a string representation of the booking request.
     *
     * @return formatted request details
     */
    @Override
    public String toString() {
        return "Guest ID: " + guestId + " | Name: " + guestName + " | Room: " + roomType;
    }
}

/**
 * SharedBookingQueue is a thread-safe queue for concurrent booking requests.
 * Synchronization ensures only one thread dequeues at a time.
 */
class SharedBookingQueue {

    /** Shared queue of booking requests. */
    private Queue<BookingRequestUC11> queue;

    /**
     * Constructor to initialize an empty shared booking queue.
     */
    public SharedBookingQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Adds a booking request to the shared queue.
     *
     * @param request the booking request to enqueue
     */
    public synchronized void enqueue(BookingRequestUC11 request) {
        queue.offer(request);
        System.out.println("Queued   : " + request);
    }

    /**
     * Retrieves and removes the next booking request from the queue.
     *
     * @return the next booking request, or null if the queue is empty
     */
    public synchronized BookingRequestUC11 dequeue() {
        return queue.poll();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if empty, false otherwise
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * SharedInventory manages thread-safe room availability and allocation tracking.
 * All critical sections are synchronized to prevent race conditions.
 */
class SharedInventory {

    /** Centralized map storing room type to available count. */
    private Map<String, Integer> inventory;

    /** Tracks allocated room IDs per room type. */
    private Map<String, Set<String>> allocatedRooms;

    /** Counters for generating unique room IDs per type. */
    private Map<String, Integer> roomCounters;

    /**
     * Constructor that initializes inventory and allocation tracking.
     */
    public SharedInventory() {
        inventory      = new HashMap<>();
        allocatedRooms = new HashMap<>();
        roomCounters   = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room",  1);
    }

    /**
     * Attempts to allocate a room for the given request in a thread-safe manner.
     * Synchronized to prevent double allocation across concurrent threads.
     *
     * @param request the booking request to process
     */
    public synchronized void allocate(BookingRequestUC11 request) {
        String roomType    = request.getRoomType();
        int    availability = inventory.getOrDefault(roomType, 0);

        if (availability > 0) {
            // Generate unique room ID
            roomCounters.put(roomType, roomCounters.getOrDefault(roomType, 0) + 1);
            String roomId = roomType.replace(" ", "") + "-" + roomCounters.get(roomType);

            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            inventory.put(roomType, availability - 1);

            System.out.println("CONFIRMED | [" + Thread.currentThread().getName() + "] "
                    + request + " | Room ID: " + roomId);
        } else {
            System.out.println("REJECTED  | [" + Thread.currentThread().getName() + "] "
                    + request + " | Reason: No availability for " + roomType);
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

    /**
     * Displays all allocated room IDs grouped by room type.
     */
    public void displayAllocatedRooms() {
        if (allocatedRooms.isEmpty()) {
            System.out.println("No rooms have been allocated.");
            return;
        }
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

/**
 * ConcurrentBookingProcessor is a Runnable that processes booking requests
 * from the shared queue in a multi-threaded environment.
 */
class ConcurrentBookingProcessor implements Runnable {

    private SharedBookingQueue sharedQueue;
    private SharedInventory    sharedInventory;

    /**
     * Constructor to initialize the processor with shared resources.
     *
     * @param sharedQueue     the shared booking request queue
     * @param sharedInventory the shared inventory service
     */
    public ConcurrentBookingProcessor(SharedBookingQueue sharedQueue,
                                      SharedInventory sharedInventory) {
        this.sharedQueue     = sharedQueue;
        this.sharedInventory = sharedInventory;
    }

    /**
     * Continuously dequeues and processes booking requests until the queue is empty.
     */
    @Override
    public void run() {
        while (!sharedQueue.isEmpty()) {
            BookingRequestUC11 request = sharedQueue.dequeue();
            if (request != null) {
                sharedInventory.allocate(request);
            }
        }
    }
}
