import java.util.*;

// Reservation class
class Reservation {
    String reservationId;
    String roomType;

    Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryManager() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Cancellation Service
class CancellationService {
    private Map<String, Reservation> reservations;
    private Stack<String> rollbackStack;
    private InventoryManager inventoryManager;

    public CancellationService(Map<String, Reservation> reservations,
                               InventoryManager inventoryManager,
                               Stack<String> rollbackStack) {
        this.reservations = reservations;
        this.inventoryManager = inventoryManager;
        this.rollbackStack = rollbackStack;
    }

    public void cancelBooking(String reservationId) {

        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        Reservation r = reservations.remove(reservationId);

        // Push to rollback stack (LIFO)
        rollbackStack.push(reservationId);

        // Restore inventory
        inventoryManager.incrementRoom(r.roomType);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + r.roomType);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation\n");

        // Existing reservations
        Map<String, Reservation> reservations = new HashMap<>();
        reservations.put("Single-1", new Reservation("Single-1", "Single"));

        // Inventory + rollback stack
        InventoryManager inventoryManager = new InventoryManager();
        Stack<String> rollbackStack = new Stack<>();

        // Cancellation service
        CancellationService service = new CancellationService(reservations, inventoryManager, rollbackStack);

        // Cancel booking
        service.cancelBooking("Single-1");

        // Print rollback history
        System.out.println("\nRollback History (Most Recent First):");
        while (!rollbackStack.isEmpty()) {
            System.out.println("Released Reservation ID: " + rollbackStack.pop());
        }

        // Updated availability
        System.out.println("\nUpdated Single Room Availability: " +
                inventoryManager.getAvailability("Single"));
    }
}