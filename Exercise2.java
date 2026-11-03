import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IT002 - LAB 3 - EXERCISE 2: TICKET MANAGEMENT FOR DAM SEN PARK
 * Implements Section 8 (Implementation Notes):
 * - Private fields with getters/setters and strict validation rules.
 * - Inheritance (Ticket -> FullPackageTicket, PartialTicket) & Polymorphism.
 * - ArrayList<Ticket> for mixed subclass objects & Scanner for console input.
 * - Normal and boundary test cases included.
 */
public class Exercise2 {

    private static final Scanner scanner = new Scanner(System.in);

    public static abstract class Ticket {
        private String ticketId;
        private String holderName;
        private int birthYear;
        private int gamesPlayed;

        public Ticket(String ticketId, String holderName, int birthYear, int gamesPlayed) {
            setTicketId(ticketId);
            setHolderName(holderName);
            setBirthYear(birthYear);
            setGamesPlayed(gamesPlayed);
        }

        public String getTicketId() {
            return ticketId;
        }

        public void setTicketId(String ticketId) {
            if (ticketId == null || ticketId.trim().isEmpty()) {
                throw new IllegalArgumentException("Ticket ID cannot be empty.");
            }
            this.ticketId = ticketId.trim();
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            if (holderName == null || holderName.trim().isEmpty()) {
                throw new IllegalArgumentException("Holder name cannot be empty.");
            }
            this.holderName = holderName.trim();
        }

        public int getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(int birthYear) {
            if (birthYear < 1900 || birthYear > 2026) {
                throw new IllegalArgumentException("Birth year must be between 1900 and 2026.");
            }
            this.birthYear = birthYear;
        }

        public int getGamesPlayed() {
            return gamesPlayed;
        }

        public void setGamesPlayed(int gamesPlayed) {
            if (gamesPlayed < 0 || gamesPlayed > 30) {
                throw new IllegalArgumentException("Games played must be between 0 and 30.");
            }
            this.gamesPlayed = gamesPlayed;
        }

        public abstract long calculatePrice();

        public abstract String getTicketType();

        @Override
        public String toString() {
            return String.format("[%s] ID: %s | Holder: %s (%d) | Games: %d | Price: %,d VND",
                    getTicketType(), ticketId, holderName, birthYear, gamesPlayed, calculatePrice());
        }
    }

    public static class FullPackageTicket extends Ticket {
        public static final long FULL_PACKAGE_PRICE = 200_000L;
        public static final int MAX_GAMES = 30;

        public FullPackageTicket(String ticketId, String holderName, int birthYear) {
            super(ticketId, holderName, birthYear, MAX_GAMES);
        }

        @Override
        public long calculatePrice() {
            return FULL_PACKAGE_PRICE;
        }

        @Override
        public String getTicketType() {
            return "Full Package";
        }
    }

    public static class PartialTicket extends Ticket {
        public static final long ENTRANCE_FEE = 70_000L;
        public static final long PRICE_PER_GAME = 20_000L;

        public PartialTicket(String ticketId, String holderName, int birthYear, int gamesPlayed) {
            super(ticketId, holderName, birthYear, gamesPlayed);
        }

        @Override
        public long calculatePrice() {
            return ENTRANCE_FEE + PRICE_PER_GAME * getGamesPlayed();
        }

        @Override
        public String getTicketType() {
            return "Partial Ticket";
        }
    }

    public static void analyzeTickets(List<Ticket> tickets) {
        System.out.println("\n--- Ticket List ---");
        long totalRevenue = 0;
        int partialCount = 0;

        for (Ticket t : tickets) {
            System.out.println("  " + t);
            totalRevenue += t.calculatePrice();
            if (t instanceof PartialTicket) {
                partialCount++;
            }
        }

        System.out.printf("-> Total revenue from all tickets sold: %,d VND%n", totalRevenue);
        System.out.println("-> Number of Partial Tickets sold: " + partialCount);
    }

    public static void runTestCases() {
        System.out.println("\n==================== [TEST EXERCISE 2] ====================");
        List<Ticket> tickets = new ArrayList<>();
        // Handout example: 1 Full Package + 1 Partial Ticket with 4 games = 350,000 VND
        tickets.add(new FullPackageTicket("T001", "Nguyen Minh Tuan", 2004));
        tickets.add(new PartialTicket("T002", "Le Hoang Nam", 2005, 4));
        // Boundary test: Partial Ticket with 0 games (entrance only = 70,000 VND)
        tickets.add(new PartialTicket("T003", "Pham Gia Huy (0 games)", 2003, 0));

        analyzeTickets(tickets);
    }

    private static int readIntMin(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min) return val;
                System.out.println("  [!] Value must be >= " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid integer. Try again.");
            }
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("  [!] Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid integer. Try again.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("  [!] Input cannot be empty.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== EXERCISE 2: TICKET MANAGEMENT FOR DAM SEN PARK ===");
        System.out.println("1. Run Sample & Boundary Test Cases");
        System.out.println("2. Input Tickets Manually via Console");
        int mode = readIntInRange("Choose mode (1-2): ", 1, 2);

        if (mode == 1) {
            runTestCases();
            return;
        }

        List<Ticket> tickets = new ArrayList<>();
        int n = readIntMin("Enter number of tickets: ", 1);
        for (int i = 0; i < n; i++) {
            System.out.println("\nInput details for ticket #" + (i + 1) + ":");
            int type = readIntInRange("Ticket Type (1 - Full Package, 2 - Partial Ticket): ", 1, 2);
            String id = readNonEmptyString("Ticket ID: ");
            String holder = readNonEmptyString("Holder Name: ");
            int birthYear = readIntInRange("Year of Birth (1900 - 2026): ", 1900, 2026);

            if (type == 1) {
                tickets.add(new FullPackageTicket(id, holder, birthYear));
            } else {
                int games = readIntInRange("Number of games played (0 - 30): ", 0, 30);
                tickets.add(new PartialTicket(id, holder, birthYear, games));
            }
        }
        analyzeTickets(tickets);
    }
}
