import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IT002 - LAB 3 - EXERCISE 3: HOTEL ROOM RENTAL MANAGEMENT
 * Implements Section 8 (Implementation Notes):
 * - Private fields with getters/setters and strict validation rules.
 * - Inheritance (Person -> Guest, Staff) and composition in Room.
 * - Add/remove guest operations & rental cost calculation.
 * - Normal and boundary test cases included.
 */
public class Exercise3 {

    private static final Scanner scanner = new Scanner(System.in);

    public static class Person {
        private String fullName;
        private int age;

        public Person(String fullName, int age) {
            setFullName(fullName);
            setAge(age);
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be empty.");
            }
            this.fullName = fullName.trim();
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            if (age <= 0 || age > 130) {
                throw new IllegalArgumentException("Age must be between 1 and 130.");
            }
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("Name: %s | Age: %d", fullName, age);
        }
    }

    public static class Guest extends Person {
        private String cccd;

        public Guest(String cccd, String fullName, int age) {
            super(fullName, age);
            setCccd(cccd);
        }

        public String getCccd() {
            return cccd;
        }

        public void setCccd(String cccd) {
            if (cccd == null || cccd.trim().isEmpty()) {
                throw new IllegalArgumentException("Citizen ID (CCCD) cannot be empty.");
            }
            this.cccd = cccd.trim();
        }

        @Override
        public String toString() {
            return String.format("[Guest] CCCD: %s | %s", cccd, super.toString());
        }
    }

    public static class Staff extends Person {
        private String licenseId;
        private String employeeId;

        public Staff(String licenseId, String employeeId, String fullName, int age) {
            super(fullName, age);
            setLicenseId(licenseId);
            setEmployeeId(employeeId);
        }

        public String getLicenseId() {
            return licenseId;
        }

        public void setLicenseId(String licenseId) {
            if (licenseId == null || licenseId.trim().isEmpty()) {
                throw new IllegalArgumentException("License ID cannot be empty.");
            }
            this.licenseId = licenseId.trim();
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            if (employeeId == null || employeeId.trim().isEmpty()) {
                throw new IllegalArgumentException("Employee ID cannot be empty.");
            }
            this.employeeId = employeeId.trim();
        }

        @Override
        public String toString() {
            return String.format("[Staff] EmpID: %s | LicenseID: %s | %s",
                    employeeId, licenseId, super.toString());
        }
    }

    public static class Room {
        private String roomType; // VIP1, VIP2, VIP3
        private int rentalDays;
        private Staff assignedStaff;
        private final List<Guest> guests;

        public Room(String roomType, int rentalDays) {
            setRoomType(roomType);
            setRentalDays(rentalDays);
            this.guests = new ArrayList<>();
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            if (roomType == null) {
                throw new IllegalArgumentException("Room type cannot be null.");
            }
            String normalized = roomType.trim().toUpperCase();
            if (!normalized.equals("VIP1") && !normalized.equals("VIP2") && !normalized.equals("VIP3")) {
                throw new IllegalArgumentException("Room type must be VIP1, VIP2, or VIP3.");
            }
            this.roomType = normalized;
        }

        public int getRentalDays() {
            return rentalDays;
        }

        public void setRentalDays(int rentalDays) {
            if (rentalDays <= 0) {
                throw new IllegalArgumentException("Rental days must be > 0.");
            }
            this.rentalDays = rentalDays;
        }

        public Staff getAssignedStaff() {
            return assignedStaff;
        }

        public void setAssignedStaff(Staff assignedStaff) {
            this.assignedStaff = assignedStaff;
        }

        public List<Guest> getGuests() {
            return new ArrayList<>(guests);
        }

        public void addGuest(Guest guest) {
            if (guest == null) {
                throw new IllegalArgumentException("Guest cannot be null.");
            }
            for (Guest g : guests) {
                if (g.getCccd().equalsIgnoreCase(guest.getCccd())) {
                    System.out.println("  [!] Guest with CCCD " + guest.getCccd() + " is already in the room.");
                    return;
                }
            }
            guests.add(guest);
        }

        public boolean removeGuestByCccd(String cccd) {
            return guests.removeIf(g -> g.getCccd().equalsIgnoreCase(cccd));
        }

        public long getDailyPrice() {
            switch (roomType) {
                case "VIP1":
                    return 900_000L;
                case "VIP2":
                    return 700_000L;
                case "VIP3":
                    return 500_000L;
                default:
                    return 0L;
            }
        }

        public long calculateRentalCost() {
            return getDailyPrice() * rentalDays;
        }

        public void displayRoomSummary() {
            System.out.println("\n--- Room Rental Summary ---");
            System.out.printf("Room Type   : %s (%,d VND/day)%n", roomType, getDailyPrice());
            System.out.printf("Rental Days : %d%n", rentalDays);
            System.out.printf("Total Cost  : %,d VND%n", calculateRentalCost());
            if (assignedStaff != null) {
                System.out.println("Staff       : " + assignedStaff);
            }
            System.out.println("Guests (" + guests.size() + ")  :");
            if (guests.isEmpty()) {
                System.out.println("  (No guests currently registered)");
            } else {
                for (Guest g : guests) {
                    System.out.println("  - " + g);
                }
            }
        }
    }

    public static void runTestCases() {
        System.out.println("\n==================== [TEST EXERCISE 3] ====================");
        // Example from handout: VIP2 room rented for 3 days = 700,000 * 3 = 2,100,000 VND
        Room room = new Room("VIP2", 3);
        room.setAssignedStaff(new Staff("LIC-9981", "EMP-01", "Tran Quoc Bao", 28));

        Guest g1 = new Guest("079204001234", "Nguyen Thi Mai", 22);
        Guest g2 = new Guest("079204005678", "Le Minh Khoi", 24);
        room.addGuest(g1);
        room.addGuest(g2);
        room.displayRoomSummary();

        System.out.println("\n-> Removing guest with CCCD 079204005678...");
        boolean removed = room.removeGuestByCccd("079204005678");
        System.out.println("-> Removed successfully: " + removed);
        room.displayRoomSummary();
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
        System.out.println("=== EXERCISE 3: HOTEL ROOM RENTAL MANAGEMENT ===");
        System.out.println("1. Run Sample & Boundary Test Cases");
        System.out.println("2. Interactive Hotel Room Management");
        int mode = readIntInRange("Choose mode (1-2): ", 1, 2);

        if (mode == 1) {
            runTestCases();
            return;
        }

        String roomType;
        while (true) {
            roomType = readNonEmptyString("Enter Room Type (VIP1 / VIP2 / VIP3): ").toUpperCase();
            if (roomType.equals("VIP1") || roomType.equals("VIP2") || roomType.equals("VIP3")) {
                break;
            }
            System.out.println("  [!] Invalid room type. Choose VIP1, VIP2, or VIP3.");
        }
        int days = readIntMin("Enter number of rental days (>= 1): ", 1);
        Room room = new Room(roomType, days);

        System.out.println("\nEnter Staff in charge:");
        String licId = readNonEmptyString("Staff License ID: ");
        String empId = readNonEmptyString("Staff Employee ID: ");
        String staffName = readNonEmptyString("Staff Full Name: ");
        int staffAge = readIntInRange("Staff Age (18 - 100): ", 18, 100);
        room.setAssignedStaff(new Staff(licId, empId, staffName, staffAge));

        while (true) {
            System.out.println("\n-- Room Operations --");
            System.out.println("1. Add Guest");
            System.out.println("2. Remove Guest by CCCD");
            System.out.println("3. Display Room Rental Summary & Total Cost");
            System.out.println("0. Exit");
            int op = readIntInRange("Select operation: ", 0, 3);
            if (op == 0) {
                break;
            } else if (op == 1) {
                String cccd = readNonEmptyString("Guest CCCD: ");
                String name = readNonEmptyString("Guest Full Name: ");
                int age = readIntInRange("Guest Age (1 - 120): ", 1, 120);
                room.addGuest(new Guest(cccd, name, age));
                System.out.println("  [+] Guest added.");
            } else if (op == 2) {
                String cccd = readNonEmptyString("Enter CCCD of guest to remove: ");
                if (room.removeGuestByCccd(cccd)) {
                    System.out.println("  [+] Guest removed.");
                } else {
                    System.out.println("  [!] Guest with CCCD " + cccd + " not found.");
                }
            } else if (op == 3) {
                room.displayRoomSummary();
            }
        }
    }
}
