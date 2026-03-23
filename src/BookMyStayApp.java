import java.util.*;

class AddOnService {
    String name;
    double cost;

    AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService service : getServices(reservationId)) {
            total += service.cost;
        }
        return total;
    }
}

public class BookMStayApp {
    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();
        String reservationId = "R101";

        manager.addService(reservationId, new AddOnService("Spa Service", 1000));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 500));

        System.out.println("Add-On Service Selection");

        List<AddOnService> services = manager.getServices(reservationId);

        for (AddOnService s : services) {
            System.out.println("Selected Service: " + s.name + " - " + (int)s.cost);
        }

        System.out.println("Reservation ID: " + reservationId);

        double total = manager.calculateTotalCost(reservationId);
        System.out.println("Total Add-On Cost: " + total);
    }
}