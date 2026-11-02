import java.util.ArrayList;
import java.util.Scanner;

/**
 * Banking Information System
 * A simple console-based Java application to manage bank accounts.
 * Concepts used: classes and objects, interface, abstract class, inheritance,
 * method overloading, ArrayList, loops, switch, and exception handling.
 */

// Interface: lists the operations every account must support
interface BankOperations {
    void deposit(double amount);
    void withdraw(double amount);
}

// Abstract class: common data and behaviour for all account types
abstract class BankAccount implements BankOperations {
    private int accountNumber;
    private String holderName;
    protected double balance;

    BankAccount(int accountNumber, String holderName, double openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = openingBalance;
    }

    // Each account type decides its own minimum balance
    abstract double getMinimumBalance();

    abstract String getAccountType();

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        balance = balance + amount;
    }

    // Method overloading: same name, different parameters
    public void deposit(double amount, String note) {
        deposit(amount);
        System.out.println("Note: " + note);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0.");
        }
        if (balance - amount < getMinimumBalance()) {
            throw new IllegalArgumentException("Insufficient balance. Minimum balance of "
                    + getMinimumBalance() + " must be maintained.");
        }
        balance = balance - amount;
    }

    public void displayDetails() {
        System.out.println("----------------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Account Holder : " + holderName);
        System.out.println("Account Type   : " + getAccountType());
        System.out.println("Balance        : " + balance);
        System.out.println("----------------------------------");
    }
}

// Subclass 1: Savings account needs a minimum balance
class SavingsAccount extends BankAccount {
    SavingsAccount(int accountNumber, String holderName, double openingBalance) {
        super(accountNumber, holderName, openingBalance);
    }

    @Override
    double getMinimumBalance() {
        return 500;
    }

    @Override
    String getAccountType() {
        return "Savings";
    }
}

// Subclass 2: Current account has no minimum balance
class CurrentAccount extends BankAccount {
    CurrentAccount(int accountNumber, String holderName, double openingBalance) {
        super(accountNumber, holderName, openingBalance);
    }

    @Override
    double getMinimumBalance() {
        return 0;
    }

    @Override
    String getAccountType() {
        return "Current";
    }
}

// Main class: menu-driven program
public class BankingInformationSystem {

    private static ArrayList<BankAccount> accounts = new ArrayList<>();
    private static int nextAccountNumber = 1001;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        System.out.println("===== BANKING INFORMATION SYSTEM =====");

        do {
            showMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        depositMoney();
                        break;
                    case 3:
                        withdrawMoney();
                        break;
                    case 4:
                        checkBalance();
                        break;
                    case 5:
                        showAccountDetails();
                        break;
                    case 6:
                        showAllAccounts();
                        break;
                    case 7:
                        System.out.println("Thank you for using the Banking Information System.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 7);
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("1. Create Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Display Account Details");
        System.out.println("6. Display All Accounts");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static double readAmount(String message) {
        System.out.print(message);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    // Finds an account by number; throws an error if it does not exist
    private static BankAccount findAccount() {
        System.out.print("Enter account number: ");
        int number = Integer.parseInt(scanner.nextLine().trim());
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == number) {
                return account;
            }
        }
        throw new IllegalArgumentException("Account not found.");
    }

    private static void createAccount() {
        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        System.out.print("Account type (1 = Savings, 2 = Current): ");
        int type = Integer.parseInt(scanner.nextLine().trim());
        double opening = readAmount("Enter opening balance: ");

        BankAccount account;
        if (type == 1) {
            if (opening < 500) {
                throw new IllegalArgumentException("Savings account needs an opening balance of at least 500.");
            }
            account = new SavingsAccount(nextAccountNumber, name, opening);
        } else if (type == 2) {
            if (opening < 0) {
                throw new IllegalArgumentException("Opening balance cannot be negative.");
            }
            account = new CurrentAccount(nextAccountNumber, name, opening);
        } else {
            throw new IllegalArgumentException("Invalid account type.");
        }

        accounts.add(account);
        nextAccountNumber++;
        System.out.println("Account created successfully. Account number: " + account.getAccountNumber());
    }

    private static void depositMoney() {
        BankAccount account = findAccount();
        double amount = readAmount("Enter amount to deposit: ");
        account.deposit(amount, "Deposit successful.");
        System.out.println("New balance: " + account.getBalance());
    }

    private static void withdrawMoney() {
        BankAccount account = findAccount();
        double amount = readAmount("Enter amount to withdraw: ");
        account.withdraw(amount);
        System.out.println("Withdrawal successful. New balance: " + account.getBalance());
    }

    private static void checkBalance() {
        BankAccount account = findAccount();
        System.out.println("Current balance: " + account.getBalance());
    }

    private static void showAccountDetails() {
        findAccount().displayDetails();
    }

    private static void showAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }
        for (BankAccount account : accounts) {
            account.displayDetails();
        }
    }
}
