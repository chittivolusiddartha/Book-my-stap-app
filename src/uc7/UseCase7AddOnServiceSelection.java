import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UseCase7AddOnServiceSelection demonstrates optional add-on service selection
 * for existing reservations using Map and List combination.
 * Core booking and inventory state remain unchanged.
 *
 * @author Your Name
 * @version 7.0
 */
public class UseCase7AddOnServiceSelection {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v7.0");
        System.out.println("========================================");

        // Initialize add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define available add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 15.00);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 30.00);
        AddOnService spa = new AddOnService("Spa Access", 50.00);
        AddOnService laundry = new AddOnService("Laundry", 10.00);
        AddOnService parking = new AddOnService("Parking", 20.00);

        // Attach services to reservations
        serviceManager.addService("G001", breakfast);
        serviceManager.addService("G001", spa);
        serviceManager.addService("G001", airportPickup);

        serviceManager.addService("G002", breakfast);
        serviceManager.addService("G002", laundry);

        serviceManager.addService("G003", parking);

        System.out.println("\n--- Add-On Services Summary ---");
        serviceManager.displayServices("G001");
        serviceManager.displayServices("G002");
        serviceManager.displayServices("G003");

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================");
    }
}

/**
 * AddOnService represents an individual optional service offering.
 */
class AddOnService {

    private String serviceName;
    private double serviceCost;

    /**
     * Constructor to initialize an add-on service.
     *
     * @param serviceName name of the service
     * @param serviceCost cost of the service in USD
     */
    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    /**
     * Returns the service name.
     *
     * @return serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns the service cost.
     *
     * @return serviceCost
     */
    public double getServiceCost() {
        return serviceCost;
    }

    /**
     * Returns a string representation of the add-on service.
     *
     * @return formatted service details
     */
    @Override
    public String toString() {
        return serviceName + " ($" + serviceCost + ")";
    }
}

/**
 * AddOnServiceManager manages the association between reservations
 * and their selected add-on services using a Map and List combination.
 */
class AddOnServiceManager {

    /** Maps reservation ID to list of selected add-on services. */
    private Map<String, List<AddOnService>> reservationServices;

    /**
     * Constructor to initialize the add-on service manager.
     */
    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    /**
     * Adds an add-on service to the specified reservation.
     *
     * @param reservationId the ID of the reservation
     * @param service       the add-on service to attach
     */
    public void addService(String reservationId, AddOnService service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
        System.out.println("Service added | Reservation: " + reservationId
                + " | Service: " + service);
    }

    /**
     * Calculates the total additional cost for a reservation's add-on services.
     *
     * @param reservationId the ID of the reservation
     * @return total cost of all selected add-on services
     */
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        if (reservationServices.containsKey(reservationId)) {
            for (AddOnService service : reservationServices.get(reservationId)) {
                total += service.getServiceCost();
            }
        }
        return total;
    }

    /**
     * Displays all selected add-on services and total cost for a reservation.
     *
     * @param reservationId the ID of the reservation
     */
    public void displayServices(String reservationId) {
        System.out.println("\nReservation ID : " + reservationId);
        if (!reservationServices.containsKey(reservationId)
                || reservationServices.get(reservationId).isEmpty()) {
            System.out.println("  No add-on services selected.");
            return;
        }
        System.out.println("  Selected Services:");
        for (AddOnService service : reservationServices.get(reservationId)) {
            System.out.println("    - " + service);
        }
        System.out.printf("  Total Add-On Cost : $%.2f%n", calculateTotalCost(reservationId));
    }
}
