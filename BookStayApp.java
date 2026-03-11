import java.util.HashMap;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Room Inventory =====");

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        System.out.println("\nChecking Availability of Single Room:");
        System.out.println("Available: " + inventory.getAvailability("Single"));
    }
}

// ===== Room Inventory Class (UC3) =====
class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();

        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void displayInventory() {
        System.out.println("Current Room Availability:");

        for (String type : availability.keySet()) {
            System.out.println(type + " Rooms: " + availability.get(type));
        }
    }
}