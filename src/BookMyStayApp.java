import java.io.*;
import java.util.*;

// Inventory class (Serializable)
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> rooms;

    public Inventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "inventory.dat";

    // Save inventory to file
    public static void saveInventory(Inventory inventory) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(inventory);
            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    // Load inventory from file
    public static Inventory loadInventory() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (Inventory) ois.readObject();

        } catch (Exception e) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return new Inventory(); // fallback
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("System Recovery");

        // Load existing state (or fresh)
        Inventory inventory = PersistenceService.loadInventory();

        // Print current inventory
        System.out.println("Current Inventory:");
        for (String type : inventory.rooms.keySet()) {
            System.out.println(type + ": " + inventory.rooms.get(type));
        }

        // Save state before exit
        PersistenceService.saveInventory(inventory);
    }
}