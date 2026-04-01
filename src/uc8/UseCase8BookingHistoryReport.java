import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UseCase8BookingHistoryReport demonstrates historical tracking of confirmed bookings
 * and report generation using a List data structure.
 * Reporting does not modify stored booking data.
 *
 * @author Your Name
 * @version 8.0
 */
public class UseCase8BookingHistoryReport {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v8.0");
        System.out.println("========================================");

        // Initialize booking history
        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed bookings being added to history
        bookingHistory.addBooking(new ConfirmedBooking("G001", "Alice",   "Single Room", "SingleRoom-1", 80.00));
        bookingHistory.addBooking(new ConfirmedBooking("G002", "Bob",     "Suite Room",  "SuiteRoom-1",  250.00));
        bookingHistory.addBooking(new ConfirmedBooking("G003", "Charlie", "Double Room", "DoubleRoom-1", 120.00));
        bookingHistory.addBooking(new ConfirmedBooking("G004", "Diana",   "Single Room", "SingleRoom-2", 80.00));
        bookingHistory.addBooking(new ConfirmedBooking("G005", "Eve",     "Suite Room",  "SuiteRoom-2",  250.00));

        // Display full booking history
        System.out.println("\n--- Full Booking History ---");
        bookingHistory.displayHistory();

        // Generate report
        System.out.println("\n--- Booking Summary Report ---");
        BookingReportService reportService = new BookingReportService(bookingHistory);
        reportService.generateSummaryReport();

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * ConfirmedBooking represents a successfully confirmed reservation record.
 */
class ConfirmedBooking {

    private String guestId;
    private String guestName;
    private String roomType;
    private String roomId;
    private double roomCost;

    /**
     * Constructor to initialize a confirmed booking.
     *
     * @param guestId   unique identifier for the guest
     * @param guestName name of the guest
     * @param roomType  type of room booked
     * @param roomId    unique room ID assigned
     * @param roomCost  cost per night for the room
     */
    public ConfirmedBooking(String guestId, String guestName,
                            String roomType, String roomId, double roomCost) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.roomCost = roomCost;
    }

    /** @return guestId */
    public String getGuestId() { return guestId; }

    /** @return guestName */
    public String getGuestName() { return guestName; }

    /** @return roomType */
    public String getRoomType() { return roomType; }

    /** @return roomId */
    public String getRoomId() { return roomId; }

    /** @return roomCost */
    public double getRoomCost() { return roomCost; }

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
                + " | Cost: $"     + roomCost;
    }
}

/**
 * BookingHistory maintains a chronological record of confirmed reservations.
 * Uses a List to preserve insertion order.
 */
class BookingHistory {

    /** Ordered list of confirmed bookings. */
    private List<ConfirmedBooking> history;

    /**
     * Constructor to initialize an empty booking history.
     */
    public BookingHistory() {
        history = new ArrayList<>();
    }

    /**
     * Adds a confirmed booking to the history.
     *
     * @param booking the confirmed booking to record
     */
    public void addBooking(ConfirmedBooking booking) {
        history.add(booking);
        System.out.println("Recorded: " + booking);
    }

    /**
     * Returns the full list of confirmed bookings.
     *
     * @return list of confirmed bookings
     */
    public List<ConfirmedBooking> getHistory() {
        return history;
    }

    /**
     * Displays all confirmed bookings in chronological order.
     */
    public void displayHistory() {
        if (history.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }
        int index = 1;
        for (ConfirmedBooking booking : history) {
            System.out.println(index++ + ". " + booking);
        }
    }
}

/**
 * BookingReportService generates summary reports from booking history.
 * Does not modify stored booking data.
 */
class BookingReportService {

    private BookingHistory bookingHistory;

    /**
     * Constructor to initialize the report service with booking history.
     *
     * @param bookingHistory the booking history to report on
     */
    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    /**
     * Generates a summary report including total bookings,
     * bookings per room type, and total revenue.
     */
    public void generateSummaryReport() {
        List<ConfirmedBooking> history = bookingHistory.getHistory();

        if (history.isEmpty()) {
            System.out.println("No data available for report.");
            return;
        }

        int totalBookings = history.size();
        double totalRevenue = 0.0;
        Map<String, Integer> bookingsPerRoomType = new HashMap<>();

        for (ConfirmedBooking booking : history) {
            totalRevenue += booking.getRoomCost();
            bookingsPerRoomType.put(
                booking.getRoomType(),
                bookingsPerRoomType.getOrDefault(booking.getRoomType(), 0) + 1
            );
        }

        System.out.println("Total Bookings  : " + totalBookings);
        System.out.printf("Total Revenue   : $%.2f%n", totalRevenue);
        System.out.println("Bookings by Room Type:");
        for (Map.Entry<String, Integer> entry : bookingsPerRoomType.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue() + " booking(s)");
        }
    }
}
