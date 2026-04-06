import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay System =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        // UC5: Add booking requests
        manager.addBookingRequest("Rohan", "Single");
        manager.addBookingRequest("Aryan", "Double");
        manager.addBookingRequest("Kiran", "Suite");
        manager.addBookingRequest("Rohan", "Single");

        // ✅ UC5: Display queue BEFORE processing
        manager.displayRequests();

        // ✅ UC6: Process bookings
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

    // ✅ UC5: Display queue (FIFO, no removal)
    public void displayRequests(){

        System.out.println("\nBooking Requests (FIFO Order):");

        for(BookingRequest request : queue){
            System.out.println(request.customerName + " -> " + request.roomType);
        }
    }

    // ✅ UC6: Processing + allocation
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