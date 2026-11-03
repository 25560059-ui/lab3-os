import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IT002 - LAB 3 - EXERCISE 4: SINGER SALARY MANAGEMENT FOR XYZ COMPANY
 * Implements Section 8 (Implementation Notes):
 * - Private fields with getters/setters and strict validation rules.
 * - Inheritance (Singer -> RegularSinger, FamousSinger) & Polymorphism.
 * - Single List<Singer> of common superclass type & Scanner for console input.
 * - Normal and boundary test cases included.
 */
public class Exercise4 {

    private static final Scanner scanner = new Scanner(System.in);

    public static abstract class Singer {
        private String fullName;
        private int yearsOfExperience;
        private int albumsSold;
        private int performances;

        public Singer(String fullName, int yearsOfExperience, int albumsSold, int performances) {
            setFullName(fullName);
            setYearsOfExperience(yearsOfExperience);
            setAlbumsSold(albumsSold);
            setPerformances(performances);
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

        public int getYearsOfExperience() {
            return yearsOfExperience;
        }

        public void setYearsOfExperience(int yearsOfExperience) {
            if (yearsOfExperience < 0) {
                throw new IllegalArgumentException("Years of experience cannot be negative.");
            }
            this.yearsOfExperience = yearsOfExperience;
        }

        public int getAlbumsSold() {
            return albumsSold;
        }

        public void setAlbumsSold(int albumsSold) {
            if (albumsSold < 0) {
                throw new IllegalArgumentException("Albums sold cannot be negative.");
            }
            this.albumsSold = albumsSold;
        }

        public int getPerformances() {
            return performances;
        }

        public void setPerformances(int performances) {
            if (performances < 0) {
                throw new IllegalArgumentException("Performances cannot be negative.");
            }
            this.performances = performances;
        }

        public abstract long calculateSalary();

        public abstract String getCategory();

        @Override
        public String toString() {
            return String.format("[%s] Name: %s | Exp: %d yrs | Albums: %d | Performances: %d | Salary: %,d VND",
                    getCategory(), fullName, yearsOfExperience, albumsSold, performances, calculateSalary());
        }
    }

    public static class RegularSinger extends Singer {
        public RegularSinger(String fullName, int yearsOfExperience, int albumsSold, int performances) {
            super(fullName, yearsOfExperience, albumsSold, performances);
        }

        @Override
        public long calculateSalary() {
            return 3_000_000L
                    + 500_000L * getYearsOfExperience()
                    + 1_000L * getAlbumsSold()
                    + 200_000L * getPerformances();
        }

        @Override
        public String getCategory() {
            return "Regular Singer";
        }
    }

    public static class FamousSinger extends Singer {
        private int gameShows;

        public FamousSinger(String fullName, int yearsOfExperience, int albumsSold, int performances, int gameShows) {
            super(fullName, yearsOfExperience, albumsSold, performances);
            setGameShows(gameShows);
        }

        public int getGameShows() {
            return gameShows;
        }

        public void setGameShows(int gameShows) {
            if (gameShows < 0) {
                throw new IllegalArgumentException("Number of game shows cannot be negative.");
            }
            this.gameShows = gameShows;
        }

        @Override
        public long calculateSalary() {
            return 5_000_000L
                    + 500_000L * getYearsOfExperience()
                    + 1_200L * getAlbumsSold()
                    + 500_000L * getPerformances()
                    + 500_000L * gameShows;
        }

        @Override
        public String getCategory() {
            return "Famous Singer";
        }

        @Override
        public String toString() {
            return String.format("[%s] Name: %s | Exp: %d yrs | Albums: %d | Performances: %d | GameShows: %d | Salary: %,d VND",
                    getCategory(), getFullName(), getYearsOfExperience(), getAlbumsSold(),
                    getPerformances(), gameShows, calculateSalary());
        }
    }

    public static void analyzeSingers(List<Singer> singers) {
        System.out.println("\n--- Singer Salary List ---");
        Singer highestPaid = null;

        for (Singer s : singers) {
            System.out.println("  " + s);
            if (highestPaid == null || s.calculateSalary() > highestPaid.calculateSalary()) {
                highestPaid = s;
            }
        }

        if (highestPaid != null) {
            System.out.printf("-> Highest-paid singer: %s (%s) with %,d VND%n",
                    highestPaid.getFullName(), highestPaid.getCategory(), highestPaid.calculateSalary());
        }
    }

    public static void runTestCases() {
        System.out.println("\n==================== [TEST EXERCISE 4] ====================");
        List<Singer> singers = new ArrayList<>();
        singers.add(new RegularSinger("Hoang Dung", 3, 500, 10));
        singers.add(new FamousSinger("Son Tung M-TP", 10, 10000, 15, 5));
        // Boundary case: 0 years, 0 albums, 0 performances
        singers.add(new RegularSinger("Newbie Singer (Boundary 0)", 0, 0, 0));

        analyzeSingers(singers);
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
        System.out.println("=== EXERCISE 4: SINGER SALARY MANAGEMENT FOR XYZ COMPANY ===");
        System.out.println("1. Run Sample & Boundary Test Cases");
        System.out.println("2. Input Singers Manually via Console");
        int mode = readIntInRange("Choose mode (1-2): ", 1, 2);

        if (mode == 1) {
            runTestCases();
            return;
        }

        List<Singer> singers = new ArrayList<>();
        int n = readIntMin("Enter number of singers: ", 1);
        for (int i = 0; i < n; i++) {
            System.out.println("\nInput details for singer #" + (i + 1) + ":");
            int type = readIntInRange("Category (1 - Regular Singer, 2 - Famous Singer): ", 1, 2);
            String name = readNonEmptyString("Full Name: ");
            int years = readIntMin("Years of experience (>= 0): ", 0);
            int albums = readIntMin("Number of albums sold (>= 0): ", 0);
            int perfs = readIntMin("Number of performances (>= 0): ", 0);

            if (type == 1) {
                singers.add(new RegularSinger(name, years, albums, perfs));
            } else {
                int shows = readIntMin("Number of game shows attended (>= 0): ", 0);
                singers.add(new FamousSinger(name, years, albums, perfs, shows));
            }
        }
        analyzeSingers(singers);
    }
}
