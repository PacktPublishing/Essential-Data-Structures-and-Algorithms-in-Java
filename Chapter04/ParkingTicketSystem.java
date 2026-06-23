import java.time.*;
import java.util.*;

public class ParkingTicketSystem {

    // Define a map of ticketNumber (String) and the entryTime (LocalDateTime)
    private final Map<String, LocalDateTime> tickets = new HashMap<>();

    public String enterGarage(String licensePlate) {
        // Generate a unique ticket key
        String ticket = UUID.randomUUID().toString();
        // Log the vehicle entry time based on the ticket key
        tickets.put(ticket, LocalDateTime.now());
        System.out.printf("Ticket issued: %s for %s%n", ticket, licensePlate);
        return ticket;
    }

    public double exitGarage(String ticket) {
        // Constant time lookup
        LocalDateTime entry = tickets.remove(ticket);
        if (entry == null) throw new IllegalArgumentException("Invalid ticket");
        long seconds = Duration.between(entry, LocalDateTime.now()).toSeconds();
        double cost = calculateFee(seconds);
        System.out.printf("Ticket %s exited. Duration: %d seconds. Cost: $%.5f%n",
                ticket, seconds, cost);
        return cost;
    }

    private double calculateFee(long seconds) {
        // Charge $2 per hour
        double ratePerHour = 2.0;
        return (seconds / 3600.0) * ratePerHour;
    }

    public static void main(String[] args) throws InterruptedException {
        ParkingTicketSystem s = new ParkingTicketSystem();
        String ticket = s.enterGarage("Z-888");
        // Introduce a 5-second real-time delay to simulate a short parking duration
        Thread.sleep(5000);
        s.exitGarage(ticket);
    }
}
