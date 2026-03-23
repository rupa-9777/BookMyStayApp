import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Inventory Manager
class InventoryManager {
    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Integer> roomCounter = new HashMap<>();

    public InventoryManager() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);

        roomCounter.put("Single", 1);
        roomCounter.put("Double", 1);
        roomCounter.put("Suite", 1);
    }

    // Critical Section (synchronized)
    public synchronized String allocateRoom(String roomType) {
        if (inventory.getOrDefault(roomType, 0) > 0) {
            int roomNum = roomCounter.get(roomType);
            roomCounter.put(roomType, roomNum + 1);

            inventory.put(roomType, inventory.get(roomType) - 1);

            return roomType + "-" + roomNum;
        }
        return null;
    }

    public void printInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private Queue<BookingRequest> queue;
    private InventoryManager inventory;

    public BookingProcessor(Queue<BookingRequest> queue, InventoryManager inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest request;

            // synchronized access to queue
            synchronized (queue) {
                if (queue.isEmpty()) break;
                request = queue.poll();
            }

            String roomId = inventory.allocateRoom(request.roomType);

            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: " +
                        request.guestName + ", Room ID: " + roomId);
            } else {
                System.out.println("Booking failed for " + request.guestName +
                        " (No " + request.roomType + " rooms available)");
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Concurrent Booking Simulation\n");

        Queue<BookingRequest> queue = new LinkedList<>();
        InventoryManager inventory = new InventoryManager();

        // Add booking requests
        queue.add(new BookingRequest("Abhi", "Single"));
        queue.add(new BookingRequest("Vanmathi", "Double"));
        queue.add(new BookingRequest("Kural", "Suite"));
        queue.add(new BookingRequest("Subha", "Single"));

        // Create threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Print remaining inventory
        inventory.printInventory();
    }
}