import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IT002 - LAB 3 - EXERCISE 1: UNIVERSITY STUDENT GRADUATION MANAGEMENT
 * Implements Section 8 (Implementation Notes):
 * - Private fields with getters/setters and strict validation rules.
 * - Inheritance (Student -> CollegeStudent, UniversityStudent) & Polymorphism.
 * - ArrayList<Student> for mixed subclass objects & Scanner for console input.
 * - Normal and boundary test cases included.
 */
public class Exercise1 {

    private static final Scanner scanner = new Scanner(System.in);

    public static abstract class Student {
        private String studentId;
        private String fullName;
        private String address;
        private int totalCredits;
        private double gpa;

        public Student(String studentId, String fullName, String address, int totalCredits, double gpa) {
            setStudentId(studentId);
            setFullName(fullName);
            setAddress(address);
            setTotalCredits(totalCredits);
            setGpa(gpa);
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            if (studentId == null || studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID cannot be empty.");
            }
            this.studentId = studentId.trim();
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be empty.");
            }
            this.address = address.trim();
        }

        public int getTotalCredits() {
            return totalCredits;
        }

        public void setTotalCredits(int totalCredits) {
            if (totalCredits < 0) {
                throw new IllegalArgumentException("Total credits cannot be negative.");
            }
            this.totalCredits = totalCredits;
        }

        public double getGpa() {
            return gpa;
        }

        public void setGpa(double gpa) {
            if (gpa < 0.0 || gpa > 10.0) {
                throw new IllegalArgumentException("GPA must be between 0.0 and 10.0.");
            }
            this.gpa = gpa;
        }

        public abstract boolean isEligibleForGraduation();

        public abstract String getProgramName();

        @Override
        public String toString() {
            return String.format("[%s] ID: %s | Name: %s | Address: %s | Credits: %d | GPA: %.2f",
                    getProgramName(), studentId, fullName, address, totalCredits, gpa);
        }
    }

    public static class CollegeStudent extends Student {
        private double graduationExamScore;

        public CollegeStudent(String studentId, String fullName, String address,
                              int totalCredits, double gpa, double graduationExamScore) {
            super(studentId, fullName, address, totalCredits, gpa);
            setGraduationExamScore(graduationExamScore);
        }

        public double getGraduationExamScore() {
            return graduationExamScore;
        }

        public void setGraduationExamScore(double graduationExamScore) {
            if (graduationExamScore < 0.0 || graduationExamScore > 10.0) {
                throw new IllegalArgumentException("Graduation exam score must be between 0.0 and 10.0.");
            }
            this.graduationExamScore = graduationExamScore;
        }

        @Override
        public boolean isEligibleForGraduation() {
            return getTotalCredits() >= 120 && getGpa() >= 5.0 && graduationExamScore >= 5.0;
        }

        @Override
        public String getProgramName() {
            return "College";
        }

        @Override
        public String toString() {
            return super.toString() + String.format(" | Exam Score: %.2f | Eligible: %s",
                    graduationExamScore, isEligibleForGraduation() ? "YES" : "NO");
        }
    }

    public static class UniversityStudent extends Student {
        private String thesisTitle;
        private double thesisScore;

        public UniversityStudent(String studentId, String fullName, String address,
                                 int totalCredits, double gpa, String thesisTitle, double thesisScore) {
            super(studentId, fullName, address, totalCredits, gpa);
            setThesisTitle(thesisTitle);
            setThesisScore(thesisScore);
        }

        public String getThesisTitle() {
            return thesisTitle;
        }

        public void setThesisTitle(String thesisTitle) {
            if (thesisTitle == null || thesisTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Thesis title cannot be empty.");
            }
            this.thesisTitle = thesisTitle.trim();
        }

        public double getThesisScore() {
            return thesisScore;
        }

        public void setThesisScore(double thesisScore) {
            if (thesisScore < 0.0 || thesisScore > 10.0) {
                throw new IllegalArgumentException("Thesis score must be between 0.0 and 10.0.");
            }
            this.thesisScore = thesisScore;
        }

        @Override
        public boolean isEligibleForGraduation() {
            return getTotalCredits() >= 170 && getGpa() >= 5.0 && thesisScore >= 5.0;
        }

        @Override
        public String getProgramName() {
            return "University";
        }

        @Override
        public String toString() {
            return super.toString() + String.format(" | Thesis: \"%s\" (%.2f) | Eligible: %s",
                    thesisTitle, thesisScore, isEligibleForGraduation() ? "YES" : "NO");
        }
    }

    public static void analyzeStudents(List<Student> students) {
        System.out.println("\n--- Student List ---");
        int eligibleCount = 0;
        UniversityStudent topUniStudent = null;

        for (Student s : students) {
            System.out.println("  " + s);
            if (s.isEligibleForGraduation()) {
                eligibleCount++;
            }
            if (s instanceof UniversityStudent) {
                UniversityStudent uni = (UniversityStudent) s;
                if (topUniStudent == null || uni.getGpa() > topUniStudent.getGpa()) {
                    topUniStudent = uni;
                }
            }
        }

        System.out.println("-> Total students eligible for graduation: " + eligibleCount + " / " + students.size());
        if (topUniStudent != null) {
            System.out.println("-> University student with highest GPA: " + topUniStudent.getFullName()
                    + " (ID: " + topUniStudent.getStudentId() + ", GPA: " + topUniStudent.getGpa() + ")");
        } else {
            System.out.println("-> No University students found in the list.");
        }
    }

    public static void runTestCases() {
        System.out.println("\n==================== [TEST EXERCISE 1] ====================");
        List<Student> students = new ArrayList<>();
        // Suggested test cases from Lab 3 handout
        students.add(new CollegeStudent("C001", "Nguyen Van A", "District 1, HCMC", 125, 6.5, 7.0));
        students.add(new UniversityStudent("U001", "Tran Thi B", "Thu Duc, HCMC", 165, 8.0, "AI in Healthcare", 9.0));
        // Boundary test cases (exact minimum thresholds: 120/5.0/5.0 and 170/5.0/5.0)
        students.add(new CollegeStudent("C002", "Le Van C (Boundary Pass)", "District 3, HCMC", 120, 5.0, 5.0));
        students.add(new UniversityStudent("U002", "Pham Thi D (Boundary Pass)", "Binh Thanh, HCMC", 170, 5.0, "Cloud Security", 5.0));
        students.add(new UniversityStudent("U003", "Hoang Van E (Top GPA)", "District 7, HCMC", 180, 9.2, "Quantum Computing", 9.5));

        analyzeStudents(students);
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

    private static double readDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("  [!] Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Try again.");
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
        System.out.println("=== EXERCISE 1: UNIVERSITY STUDENT GRADUATION MANAGEMENT ===");
        System.out.println("1. Run Sample & Boundary Test Cases");
        System.out.println("2. Input Students Manually via Console");
        int mode = readIntInRange("Choose mode (1-2): ", 1, 2);

        if (mode == 1) {
            runTestCases();
            return;
        }

        List<Student> students = new ArrayList<>();
        int n = readIntMin("Enter number of students: ", 1);
        for (int i = 0; i < n; i++) {
            System.out.println("\nInput details for student #" + (i + 1) + ":");
            int type = readIntInRange("Program (1 - College, 2 - University): ", 1, 2);
            String id = readNonEmptyString("Student ID: ");
            String name = readNonEmptyString("Full Name: ");
            String address = readNonEmptyString("Address: ");
            int credits = readIntMin("Total Credits (>= 0): ", 0);
            double gpa = readDoubleInRange("GPA (0.0 - 10.0): ", 0.0, 10.0);

            if (type == 1) {
                double examScore = readDoubleInRange("Graduation Exam Score (0.0 - 10.0): ", 0.0, 10.0);
                students.add(new CollegeStudent(id, name, address, credits, gpa, examScore));
            } else {
                String thesisTitle = readNonEmptyString("Thesis Title: ");
                double thesisScore = readDoubleInRange("Thesis Score (0.0 - 10.0): ", 0.0, 10.0);
                students.add(new UniversityStudent(id, name, address, credits, gpa, thesisTitle, thesisScore));
            }
        }
        analyzeStudents(students);
    }
}
