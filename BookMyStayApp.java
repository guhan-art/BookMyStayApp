import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay System =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        manager.addBookingRequest("Rohan", "Single");
        manager.addBookingRequest("Aryan", "Double");
        manager.addBookingRequest("Kiran", "Suite");
        manager.addBookingRequest("Rohan", "Single");

        manager.displayRequests();

        manager.processBookings();

        inventory.displayInventory();
    }
}

class RoomInventory {

    private HashMap<String,Integer> availability;

    public RoomInventory(){

        availability = new HashMap<>();

        availability.put("Single",2);
        availability.put("Double",1);
        availability.put("Suite",1);
    }

    public boolean bookRoom(String type){

        if(availability.getOrDefault(type,0) > 0){

            availability.put(type,availability.get(type)-1);
            return true;
        }

        return false;
    }

    public void displayInventory(){

        System.out.println("\nRemaining Rooms:");

        for(String type : availability.keySet()){
            System.out.println(type + " : " + availability.get(type));
        }
    }
}

class BookingManager {

    private Queue<BookingRequest> queue;
    private RoomInventory inventory;
    private Set<String> confirmedCustomers;

    public BookingManager(RoomInventory inventory){

        this.inventory = inventory;

        queue = new LinkedList<>();
        confirmedCustomers = new HashSet<>();
    }

    public void addBookingRequest(String name,String roomType){

        queue.add(new BookingRequest(name,roomType));

        System.out.println("Booking request added : " + name);
    }

    public void displayRequests(){

        System.out.println("\nBooking Requests (FIFO Order):");

        for(BookingRequest request : queue){
            System.out.println(request.customerName + " -> " + request.roomType);
        }
    }

    public void processBookings(){

        System.out.println("\nProcessing bookings...");

        while(!queue.isEmpty()){

            BookingRequest request = queue.poll();

            if(confirmedCustomers.contains(request.customerName)){

                System.out.println("Duplicate booking rejected : " + request.customerName);
                continue;
            }

            if(inventory.bookRoom(request.roomType)){

                confirmedCustomers.add(request.customerName);

                System.out.println("Booking confirmed : " + request.customerName);
            }

            else{

                System.out.println("No rooms available for " + request.customerName);
            }
        }
    }
}

class BookingRequest{

    String customerName;
    String roomType;

    public BookingRequest(String name,String room){

        this.customerName = name;
        this.roomType = room;
    }
}
// ------------------ UC6: Reservation Confirmation & Room Allocation ------------------

class BookingManagerUC6 {

    private Queue<BookingRequest> queue;
    private RoomInventory inventory;

    private Set<String> allocatedRoomIds; // To ensure unique room IDs
    private HashMap<String, Set<String>> roomAllocationMap; // Room type -> allocated IDs

    public BookingManagerUC6(RoomInventory inventory, Queue<BookingRequest> existingQueue) {
        this.inventory = inventory;
        this.queue = existingQueue; // Use existing queue from UC5
        this.allocatedRoomIds = new HashSet<>();
        this.roomAllocationMap = new HashMap<>();
    }

    public void processBookings() {
        System.out.println("\n=== UC6: Processing Bookings with Unique Room IDs ===");

        while (!queue.isEmpty()) {
            BookingRequest request = queue.poll();
            String type = request.roomType;

            // Step 1: Check room availability
            if (!inventory.isAvailable(type)) {
                System.out.println("No rooms available for " + request.customerName);
                continue;
            }

            // Step 2: Generate unique Room ID
            String roomId = generateRoomId(type);

            // Step 3: Allocate room (atomic operation)
            allocatedRoomIds.add(roomId);

            roomAllocationMap.putIfAbsent(type, new HashSet<>());
            roomAllocationMap.get(type).add(roomId);

            // Step 4: Update inventory
            inventory.reduceRoom(type);

            // Step 5: Confirm booking
            System.out.println("Booking confirmed: " + request.customerName + " -> Room ID: " + roomId);
        }
    }

    // Generates a unique room ID per type (S1, S2, D1, SU1, etc.)
    private String generateRoomId(String type) {
        int count = roomAllocationMap.getOrDefault(type, new HashSet<>()).size() + 1;
        if (type.length() >= 2)
            return type.substring(0, 2).toUpperCase() + count; // SU1, DO1
        else
            return type.substring(0, 1).toUpperCase() + count;  // S1
    }
}

// Extend RoomInventory for UC6 if needed
class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public boolean isAvailable(String type) {
        return availability.getOrDefault(type, 0) > 0;
    }

    public void reduceRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nRemaining Rooms:");
        for (String type : availability.keySet()) {
            System.out.println(type + " : " + availability.get(type));
        }
    }
}
// ------------------ UC7: Add-On Service Selection ------------------

class Service {
    String name;
    double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class AddOnServiceManager {

    private Map<String, List<Service>> reservationServices; // reservationID -> list of services

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach one or more services to a reservation
    public void addServices(String reservationId, List<Service> services) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).addAll(services);
        System.out.println("Added " + services.size() + " service(s) to reservation " + reservationId);
    }

    // Calculate total cost of add-on services for a reservation
    public double calculateTotalCost(String reservationId) {
        List<Service> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        double total = 0;
        for (Service s : services) {
            total += s.price;
        }
        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        System.out.println("Reservation " + reservationId + " Add-On Services:");
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }
        for (Service s : services) {
            System.out.println("- " + s.name + " : $" + s.price);
        }
        System.out.println("Total Additional Cost: $" + calculateTotalCost(reservationId));
    }
}