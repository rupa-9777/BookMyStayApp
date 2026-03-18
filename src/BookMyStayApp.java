import java.util.*;

class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

class BookingService {

    private Queue<Reservation> queue = new LinkedList<>();
    private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();
    private RoomInventory inventory;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    void addRequest(Reservation r) {
        queue.add(r);
    }

    void processBookings() {

        System.out.println("Room Allocation Processing");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();

            if (inventory.getAvailability(r.roomType) > 0) {

                String roomId = generateRoomId(r.roomType);

                allocatedRooms
                        .computeIfAbsent(r.roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrement(r.roomType);

                System.out.println(
                        "Booking confirmed for Guest: "
                                + r.guestName
                                + ", Room ID: "
                                + roomId
                );
            }
        }
    }

    private String generateRoomId(String type) {

        Set<String> set = allocatedRooms.getOrDefault(type, new HashSet<>());
        int number = set.size() + 1;

        return type + "-" + number;
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingService service = new BookingService(inventory);

        service.addRequest(new Reservation("Abhi", "Single"));
        service.addRequest(new Reservation("Subha", "Single"));
        service.addRequest(new Reservation("Vanmathi", "Suite"));

        service.processBookings();
    }
}