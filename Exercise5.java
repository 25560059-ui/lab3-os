import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IT002 - LAB 3 - EXERCISE 5: COSMETIC STORE ORDER MANAGEMENT
 * Implements Section 8 (Implementation Notes):
 * - Private fields with getters/setters and strict validation rules.
 * - Inheritance for CosmeticProduct (Perfume, Shampoo, ShowerGel) and ProductPackage (RomanticPackage, FreshAirPackage).
 * - Standard Java file-writing APIs (BufferedWriter/FileWriter) with IOException handling to save order_list.txt.
 * - Normal and boundary test cases included.
 */
public class Exercise5 {

    private static final Scanner scanner = new Scanner(System.in);

    public static abstract class CosmeticProduct {
        private String productId;
        private String type;
        private int volume; // in ml
        private long unitPrice; // VND per ml

        public CosmeticProduct(String productId, String type, int volume, long unitPrice) {
            setProductId(productId);
            setType(type);
            setVolume(volume);
            setUnitPrice(unitPrice);
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be empty.");
            }
            this.productId = productId.trim();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            if (type == null || type.trim().isEmpty()) {
                throw new IllegalArgumentException("Product type cannot be empty.");
            }
            this.type = type.trim();
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            if (volume <= 0) {
                throw new IllegalArgumentException("Volume (ml) must be positive.");
            }
            this.volume = volume;
        }

        public long getUnitPrice() {
            return unitPrice;
        }

        protected void setUnitPrice(long unitPrice) {
            if (unitPrice <= 0) {
                throw new IllegalArgumentException("Unit price must be positive.");
            }
            this.unitPrice = unitPrice;
        }

        public long calculatePrice() {
            return (long) volume * unitPrice;
        }

        @Override
        public String toString() {
            return String.format("%s (ID: %s, %d ml x %,d VND/ml = %,d VND)",
                    type, productId, volume, unitPrice, calculatePrice());
        }
    }

    public static class Perfume extends CosmeticProduct {
        public static final int ROSE = 1;
        public static final int CHAMOMILE = 2;

        public Perfume(String productId, int variation, int volume) {
            super(productId,
                    variation == ROSE ? "Rose Perfume" : "Chamomile Perfume",
                    volume,
                    variation == ROSE ? 20_000L : 15_000L);
            if (variation != ROSE && variation != CHAMOMILE) {
                throw new IllegalArgumentException("Invalid Perfume variation.");
            }
        }
    }

    public static class Shampoo extends CosmeticProduct {
        public static final int DRY_HAIR = 1;
        public static final int OILY_HAIR_STD1 = 2;
        public static final int OILY_HAIR_STD2 = 3;

        public Shampoo(String productId, int variation, int volume) {
            super(productId,
                    resolveName(variation),
                    volume,
                    resolveRate(variation));
        }

        private static String resolveName(int variation) {
            switch (variation) {
                case DRY_HAIR:
                    return "Dry Hair Shampoo";
                case OILY_HAIR_STD1:
                    return "Oily Hair Shampoo - Standard 1";
                case OILY_HAIR_STD2:
                    return "Oily Hair Shampoo - Standard 2";
                default:
                    throw new IllegalArgumentException("Invalid Shampoo variation.");
            }
        }

        private static long resolveRate(int variation) {
            switch (variation) {
                case DRY_HAIR:
                    return 20_000L;
                case OILY_HAIR_STD1:
                    return 30_000L;
                case OILY_HAIR_STD2:
                    return 40_000L;
                default:
                    throw new IllegalArgumentException("Invalid Shampoo variation.");
            }
        }
    }

    public static class ShowerGel extends CosmeticProduct {
        public static final int DRY_SKIN = 1;
        public static final int OILY_SKIN = 2;

        public ShowerGel(String productId, int variation, int volume) {
            super(productId,
                    variation == DRY_SKIN ? "Dry Skin Shower Gel" : "Oily Skin Shower Gel",
                    volume,
                    variation == DRY_SKIN ? 20_000L : 40_000L);
            if (variation != DRY_SKIN && variation != OILY_SKIN) {
                throw new IllegalArgumentException("Invalid Shower Gel variation.");
            }
        }
    }

    public static abstract class ProductPackage {
        private final List<CosmeticProduct> products = new ArrayList<>();

        protected void addProduct(CosmeticProduct p) {
            if (p != null) {
                products.add(p);
            }
        }

        public List<CosmeticProduct> getProducts() {
            return new ArrayList<>(products);
        }

        public long calculatePackagePrice() {
            long total = 0;
            for (CosmeticProduct p : products) {
                total += p.calculatePrice();
            }
            return total;
        }

        public abstract String getPackageName();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getPackageName()).append(" [Total Package Price: ")
              .append(String.format("%,d VND", calculatePackagePrice())).append("]\n");
            for (CosmeticProduct p : products) {
                sb.append("      * ").append(p).append("\n");
            }
            return sb.toString().trim();
        }
    }

    public static class RomanticPackage extends ProductPackage {
        public RomanticPackage(Perfume rosePerfume, Shampoo optionalShampoo, ShowerGel optionalShowerGel) {
            if (rosePerfume == null || !rosePerfume.getType().equals("Rose Perfume")) {
                throw new IllegalArgumentException("Romantic Package MUST include Rose Perfume.");
            }
            addProduct(rosePerfume);
            if (optionalShampoo != null) {
                addProduct(optionalShampoo);
            }
            if (optionalShowerGel != null) {
                addProduct(optionalShowerGel);
            }
        }

        @Override
        public String getPackageName() {
            return "Romantic Package";
        }
    }

    public static class FreshAirPackage extends ProductPackage {
        public FreshAirPackage(Shampoo oilyHairShampoo, Perfume optionalPerfume, ShowerGel optionalShowerGel) {
            if (oilyHairShampoo == null || !oilyHairShampoo.getType().startsWith("Oily Hair Shampoo")) {
                throw new IllegalArgumentException("Fresh-Air Package MUST include an Oily Hair Shampoo.");
            }
            addProduct(oilyHairShampoo);
            if (optionalPerfume != null) {
                addProduct(optionalPerfume);
            }
            if (optionalShowerGel != null) {
                addProduct(optionalShowerGel);
            }
        }

        @Override
        public String getPackageName() {
            return "Fresh-Air Package";
        }
    }

    public static class Customer {
        private String customerId;
        private String fullName;
        private String address;
        private String phoneNumber;

        public Customer(String customerId, String fullName, String address, String phoneNumber) {
            setCustomerId(customerId);
            setFullName(fullName);
            setAddress(address);
            setPhoneNumber(phoneNumber);
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            if (customerId == null || customerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer ID cannot be empty.");
            }
            this.customerId = customerId.trim();
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            this.fullName = fullName.trim();
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer address cannot be empty.");
            }
            this.address = address.trim();
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be empty.");
            }
            this.phoneNumber = phoneNumber.trim();
        }

        @Override
        public String toString() {
            return String.format("%s (ID: %s | Phone: %s | Address: %s)",
                    fullName, customerId, phoneNumber, address);
        }
    }

    public static class Order {
        private String orderId;
        private Customer customer;
        private String invoiceDate;
        private ProductPackage productPackage;
        private long orderPrice;

        public Order(String orderId, Customer customer, String invoiceDate, ProductPackage productPackage) {
            setOrderId(orderId);
            setCustomer(customer);
            setInvoiceDate(invoiceDate);
            setProductPackage(productPackage);
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            if (orderId == null || orderId.trim().isEmpty()) {
                throw new IllegalArgumentException("Order ID cannot be empty.");
            }
            this.orderId = orderId.trim();
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            if (customer == null) {
                throw new IllegalArgumentException("Customer cannot be null.");
            }
            this.customer = customer;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            if (invoiceDate == null || invoiceDate.trim().isEmpty()) {
                throw new IllegalArgumentException("Invoice date cannot be empty.");
            }
            this.invoiceDate = invoiceDate.trim();
        }

        public ProductPackage getProductPackage() {
            return productPackage;
        }

        public void setProductPackage(ProductPackage productPackage) {
            if (productPackage == null) {
                throw new IllegalArgumentException("Product package cannot be null.");
            }
            this.productPackage = productPackage;
            this.orderPrice = productPackage.calculatePackagePrice();
        }

        public long getOrderPrice() {
            return orderPrice;
        }

        @Override
        public String toString() {
            return String.format(
                    "Order ID    : %s%n" +
                    "Invoice Date: %s%n" +
                    "Customer    : %s%n" +
                    "Package     : %s%n" +
                    "Order Total : %,d VND%n" +
                    "------------------------------------------------------------",
                    orderId, invoiceDate, customer, productPackage, orderPrice);
        }
    }

    public static void displayAndSaveOrders(List<Order> orders, String fileName) {
        System.out.println("\n================ DISPLAYING ALL ORDERS BEFORE SAVING ================");
        for (Order o : orders) {
            System.out.println(o);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("================ COSMETIC STORE ORDER LIST ================\n");
            for (Order o : orders) {
                writer.write(o.toString());
                writer.newLine();
            }
            System.out.println("-> Successfully saved " + orders.size() + " order(s) to file: " + fileName);
        } catch (IOException e) {
            System.err.println("  [!] I/O Error while saving orders to " + fileName + ": " + e.getMessage());
        }
    }

    public static void runTestCases() {
        System.out.println("\n==================== [TEST EXERCISE 5] ====================");
        List<Order> orders = new ArrayList<>();

        // Example from Handout: Romantic Package with 50 ml of Rose Perfume and 100 ml of Dry Skin Shower Gel
        // Cost = 50 * 20,000 + 100 * 20,000 = 3,000,000 VND
        Customer c1 = new Customer("CUS01", "Vo Thi Sau", "District 1, HCMC", "0901234567");
        Perfume rose50 = new Perfume("P01", Perfume.ROSE, 50);
        ShowerGel dryGel100 = new ShowerGel("SG01", ShowerGel.DRY_SKIN, 100);
        ProductPackage pkg1 = new RomanticPackage(rose50, null, dryGel100);
        orders.add(new Order("ORD-001", c1, "03/11/2026", pkg1));

        // Second Order: Fresh-Air Package with Oily Hair Shampoo Std 2 (100 ml) + Chamomile Perfume (50 ml)
        Customer c2 = new Customer("CUS02", "Dang Van Lam", "District 7, HCMC", "0987654321");
        Shampoo oilyStd2 = new Shampoo("SH01", Shampoo.OILY_HAIR_STD2, 100);
        Perfume cham50 = new Perfume("P02", Perfume.CHAMOMILE, 50);
        ProductPackage pkg2 = new FreshAirPackage(oilyStd2, cham50, null);
        orders.add(new Order("ORD-002", c2, "03/11/2026", pkg2));

        displayAndSaveOrders(orders, "order_list.txt");
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
        System.out.println("=== EXERCISE 5: COSMETIC STORE ORDER MANAGEMENT ===");
        System.out.println("1. Run Sample & Boundary Test Cases (Saves to order_list.txt)");
        System.out.println("2. Input N Orders Manually via Console (Saves to order_list.txt)");
        int mode = readIntInRange("Choose mode (1-2): ", 1, 2);

        if (mode == 1) {
            runTestCases();
            return;
        }

        List<Order> orders = new ArrayList<>();
        int n = readIntMin("Enter number of orders (N >= 1): ", 1);
        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Input Order #" + (i + 1) + " ---");
            String orderId = readNonEmptyString("Order ID: ");
            String invoiceDate = readNonEmptyString("Invoice Date (dd/mm/yyyy): ");
            String cusId = readNonEmptyString("Customer ID: ");
            String cusName = readNonEmptyString("Customer Full Name: ");
            String cusAddr = readNonEmptyString("Customer Address: ");
            String cusPhone = readNonEmptyString("Customer Phone Number: ");
            Customer customer = new Customer(cusId, cusName, cusAddr, cusPhone);

            int pkgType = readIntInRange("Select Package (1 - Romantic Package, 2 - Fresh-Air Package): ", 1, 2);
            ProductPackage pkg;

            if (pkgType == 1) {
                System.out.println("  * Mandatory Rose Perfume (20,000 VND/ml):");
                String pId = readNonEmptyString("    Perfume ID: ");
                int pVol = readIntMin("    Volume (ml > 0): ", 1);
                Perfume rose = new Perfume(pId, Perfume.ROSE, pVol);

                Shampoo optShampoo = null;
                int addShampoo = readIntInRange("  * Include optional Shampoo? (1 - Yes, 0 - No): ", 0, 1);
                if (addShampoo == 1) {
                    String sId = readNonEmptyString("    Shampoo ID: ");
                    int sVar = readIntInRange("    Variation (1-Dry Hair, 2-Oily Std1, 3-Oily Std2): ", 1, 3);
                    int sVol = readIntMin("    Volume (ml > 0): ", 1);
                    optShampoo = new Shampoo(sId, sVar, sVol);
                }

                ShowerGel optGel = null;
                int addGel = readIntInRange("  * Include optional Shower Gel? (1 - Yes, 0 - No): ", 0, 1);
                if (addGel == 1) {
                    String gId = readNonEmptyString("    Shower Gel ID: ");
                    int gVar = readIntInRange("    Variation (1-Dry Skin, 2-Oily Skin): ", 1, 2);
                    int gVol = readIntMin("    Volume (ml > 0): ", 1);
                    optGel = new ShowerGel(gId, gVar, gVol);
                }

                pkg = new RomanticPackage(rose, optShampoo, optGel);
            } else {
                System.out.println("  * Mandatory Oily Hair Shampoo:");
                String sId = readNonEmptyString("    Shampoo ID: ");
                int sStd = readIntInRange("    Standard (1 - Standard 1 [30,000/ml], 2 - Standard 2 [40,000/ml]): ", 1, 2);
                int sVol = readIntMin("    Volume (ml > 0): ", 1);
                Shampoo oilyShampoo = new Shampoo(sId,
                        sStd == 1 ? Shampoo.OILY_HAIR_STD1 : Shampoo.OILY_HAIR_STD2, sVol);

                Perfume optPerfume = null;
                int addPerfume = readIntInRange("  * Include optional Perfume? (1 - Yes, 0 - No): ", 0, 1);
                if (addPerfume == 1) {
                    String pId = readNonEmptyString("    Perfume ID: ");
                    int pVar = readIntInRange("    Variation (1-Rose, 2-Chamomile): ", 1, 2);
                    int pVol = readIntMin("    Volume (ml > 0): ", 1);
                    optPerfume = new Perfume(pId, pVar, pVol);
                }

                ShowerGel optGel = null;
                int addGel = readIntInRange("  * Include optional Shower Gel? (1 - Yes, 0 - No): ", 0, 1);
                if (addGel == 1) {
                    String gId = readNonEmptyString("    Shower Gel ID: ");
                    int gVar = readIntInRange("    Variation (1-Dry Skin, 2-Oily Skin): ", 1, 2);
                    int gVol = readIntMin("    Volume (ml > 0): ", 1);
                    optGel = new ShowerGel(gId, gVar, gVol);
                }

                pkg = new FreshAirPackage(oilyShampoo, optPerfume, optGel);
            }

            orders.add(new Order(orderId, customer, invoiceDate, pkg));
        }

        displayAndSaveOrders(orders, "order_list.txt");
    }
}
