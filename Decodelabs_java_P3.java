import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * BankATM.java
 *
 * Single-file version of the ATM application. Combines Main, ATM, and
 * BankAccount into one file. Only one top-level class in a file may be
 * public, so Main is public and the others are package-private — this
 * compiles and runs exactly like the original three-file version.
 *
 * Compile: javac BankATM.java
 * Run:     java Main
 */
public class Decodelabs_java_P3{
    // Intentionally empty — this class exists only so the filename
    // (BankATM.java) doesn't have to match a public class name.
    // The real entry point is the Main class below.
}

/**
 * Main.java
 *
 * Entry point of the application. Sets up a couple of sample accounts
 * and launches the ATM.
 */
class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ATM atm = new ATM(scanner);

            // Sample seed accounts (account number, holder name, starting balance)
            atm.addAccount(new BankAccount("1001", "Alice Johnson", 500.00));
            atm.addAccount(new BankAccount("1002", "Brian Smith", 1250.50));
            atm.addAccount(new BankAccount("1003", "Chitra Rao", 75.00));

            atm.start();
        }
    }
}

/**
 * ATM.java
 *
 * Represents the ATM machine itself. This class is responsible for the
 * human-facing side of things: showing menus, reading input, and
 * validating it. It does NOT contain banking business rules — it simply
 * delegates to the BankAccount class for that (Separation of Concerns).
 */
class ATM {

    private final Map<String, BankAccount> accounts;
    private final Scanner scanner;
    private BankAccount currentAccount;

    public ATM(Scanner scanner) {
        this.scanner = scanner;
        this.accounts = new HashMap<>();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    /** Starts the main ATM loop: login, then menu, until the user exits. */
    public void start() {
        printBanner();

        if (!authenticateUser()) {
            System.out.println("Too many failed attempts. Card retained. Goodbye.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readMenuChoice();

            switch (choice) {
                case 1:
                    handleBalanceCheck();
                    break;
                case 2:
                    handleDeposit();
                    break;
                case 3:
                    handleWithdrawal();
                    break;
                case 4:
                    System.out.println("\nThank you for banking with us. Please take your card. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a number between 1 and 4.");
            }
        }
    }

    // ---------------------------------------------------------------
    // Authentication (simple account-number "login")
    // ---------------------------------------------------------------
    private boolean authenticateUser() {
        int attempts = 0;
        final int maxAttempts = 3;

        while (attempts < maxAttempts) {
            System.out.print("Enter your account number: ");
            String accNum = scanner.nextLine().trim();

            BankAccount account = accounts.get(accNum);
            if (account != null) {
                currentAccount = account;
                System.out.println("Welcome, " + account.getAccountHolderName() + "!");
                return true;
            }

            attempts++;
            System.out.println("Account not found. Attempts remaining: " + (maxAttempts - attempts));
        }
        return false;
    }

    // ---------------------------------------------------------------
    // Menu handling
    // ---------------------------------------------------------------
    private void printBanner() {
        System.out.println("========================================");
        System.out.println("      WELCOME TO DECODELABS ATM");
        System.out.println("========================================");
    }

    private void printMenu() {
        System.out.println("\n----------------------------------------");
        System.out.println("  1. Check Balance");
        System.out.println("  2. Deposit Money");
        System.out.println("  3. Withdraw Money");
        System.out.println("  4. Exit");
        System.out.println("----------------------------------------");
        System.out.print("Choose an option (1-4): ");
    }

    /**
     * Reads a menu choice safely. Never crashes on bad input (e.g. letters),
     * always re-prompts until a valid integer is provided.
     */
    private int readMenuChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("That's not a valid number. Please enter a number between 1 and 4: ");
            scanner.next(); // consume the invalid token so we don't loop forever
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return choice;
    }

    /**
     * Reads a monetary amount safely. Guards against non-numeric input
     * and negative numbers before ever handing the value to BankAccount.
     */
    private double readAmount(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid amount. Please enter a valid number (e.g. 50.00): ");
            scanner.next(); // clear the bad token from the buffer
        }
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline

        while (amount <= 0) {
            System.out.print("Amount must be greater than zero. Please try again: ");
            while (!scanner.hasNextDouble()) {
                System.out.print("Invalid amount. Please enter a valid number: ");
                scanner.next();
            }
            amount = scanner.nextDouble();
            scanner.nextLine();
        }
        return amount;
    }

    // ---------------------------------------------------------------
    // Transaction handlers — each delegates the actual logic/rules
    // to the BankAccount object.
    // ---------------------------------------------------------------
    private void handleBalanceCheck() {
        System.out.printf("%nYour current balance is: $%.2f%n", currentAccount.getBalance());
    }

    private void handleDeposit() {
        double amount = readAmount("Enter amount to deposit: $");
        BankAccount.TransactionResult result = currentAccount.deposit(amount);
        System.out.println(result.getMessage());
    }

    private void handleWithdrawal() {
        double amount = readAmount("Enter amount to withdraw: $");
        BankAccount.TransactionResult result = currentAccount.withdraw(amount);
        System.out.println(result.getMessage());
    }
}

/**
 * BankAccount.java
 *
 * Represents a single bank account.
 * All internal state is private (encapsulated) — the only way to read or
 * change it is through the public methods below, which enforce the
 * business rules (no negative amounts, no overdrafts, etc.).
 */
class BankAccount {

    // ---- Private state: the "vault". Nothing outside this class can
    // touch these fields directly. ----
    private final String accountNumber;
    private final String accountHolderName;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = Math.max(initialBalance, 0.0);
    }

    // ---- Getters (read-only access) ----
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Deposit money into the account.
     * @param amount amount to deposit (must be > 0)
     * @return a result message describing the outcome
     */
    public TransactionResult deposit(double amount) {
        if (amount <= 0) {
            return new TransactionResult(false, "Deposit failed: amount must be greater than zero.");
        }
        balance += amount;
        return new TransactionResult(true,
                String.format("Deposit successful. New balance: $%.2f", balance));
    }

    /**
     * Withdraw money from the account.
     * @param amount amount to withdraw (must be > 0 and <= balance)
     * @return a result message describing the outcome
     */
    public TransactionResult withdraw(double amount) {
        if (amount <= 0) {
            return new TransactionResult(false, "Withdrawal failed: amount must be greater than zero.");
        }
        if (amount > balance) {
            return new TransactionResult(false,
                    String.format("Withdrawal failed: insufficient balance. Current balance: $%.2f", balance));
        }
        balance -= amount;
        return new TransactionResult(true,
                String.format("Withdrawal successful. New balance: $%.2f", balance));
    }

    /**
     * Simple immutable holder for the outcome of a transaction,
     * so the caller knows whether it succeeded and what message to show.
     */
    public static class TransactionResult {
        private final boolean success;
        private final String message;

        public TransactionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
