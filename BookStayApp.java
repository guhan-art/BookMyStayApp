import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - UC4 FIFO Booking =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager bookingManager = new BookingManager(inventory);

        // Adding booking requests
        bookingManager.addBookingRequest("Rohan", "Single");
        bookingManager.addBookingRequest("Aryan", "Double");
        bookingManager.addBookingRequest("Kiran", "Suite");

        // Process all bookings (FIFO)
        bookingManager.processBookings();

        inventory.displayInventory();
    }
}
// ===== UC3 - Room Inventory =====
class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public boolean bookRoom(String type) {
        if (availability.getOrDefault(type, 0) > 0) {
            availability.put(type, availability.get(type) - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nUpdated Room Availability:");
        for (String type : availability.keySet()) {
            System.out.println(type + ": " + availability.get(type));
        }
    }
}
// ===== UC4 - Booking Manager (FIFO Queue) =====
class BookingManager {

    private Queue<BookingRequest> requestQueue;
    private RoomInventory inventory;

    public BookingManager(RoomInventory inventory) {
        this.inventory = inventory;
        requestQueue = new LinkedList<>();
    }

    public void addBookingRequest(String customerName, String roomType) {
        requestQueue.add(new BookingRequest(customerName, roomType));
        System.out.println("Booking request added for " + customerName);
    }

    public void processBookings() {

        System.out.println("\nProcessing Bookings (FIFO Order):");

        while (!requestQueue.isEmpty()) {

            BookingRequest request = requestQueue.poll();

            if (inventory.bookRoom(request.roomType)) {
                System.out.println("Booking confirmed for " + request.customerName);
            } else {
                System.out.println("Booking failed for " + request.customerName + " (No rooms available)");
            }
        }
    }
}
// ===== Booking Request Model =====
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}