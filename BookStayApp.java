public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" Welcome to Book My Stay v1.0 ");
        System.out.println(" Hotel Booking Management System ");
        System.out.println("=================================");

        System.out.println("Application Started Successfully.");
    }
}
abstract class Room {

    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
    }
}
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 2000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 3500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 6000);
    }
}
int singleAvailable = 5;
int doubleAvailable = 3;
int suiteAvailable = 2;

System.out.println("Available Single Rooms: " + singleAvailable);