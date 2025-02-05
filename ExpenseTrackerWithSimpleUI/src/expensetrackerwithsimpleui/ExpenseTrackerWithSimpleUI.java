package expensetrackerwithsimpleui;

import java.io.*;
import java.util.*;

class User implements Serializable {
    private String username;
    private String password;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { return username; }
    public boolean checkPassword(String password) { return this.password.equals(password); }
}

class Expense implements Serializable {
    private String date;
    private String category;
    private double amount;
    
    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }
    
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    
    @Override
    public String toString() {
        return "Date: " + date + ", Category: " + category + ", Amount: " + amount;
    }
}

public class ExpenseTrackerWithSimpleUI {
    private static final String EXPENSE_FILE = "expenses.dat";
    private static final String USER_FILE = "users.dat";
    private List<Expense> expenses;
    private List<User> users;
    private Scanner scanner;
    private User loggedInUser;
    
    public ExpenseTrackerWithSimpleUI() {
        expenses = loadExpenses();
        users = loadUsers();
        scanner = new Scanner(System.in);
    }
    
    private List<Expense> loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXPENSE_FILE))) {
            return (List<Expense>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
    private void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EXPENSE_FILE))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            System.out.println("Error saving expenses.");
        }
    }
    
    private List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }
    
    private void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        
        users.add(new User(username, password));
        saveUsers();
        System.out.println("User registered successfully.");
    }
    
    private boolean loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                loggedInUser = user;
                System.out.println("Login successful.");
                return true;
            }
        }
        System.out.println("Invalid credentials. Try again.");
        return false;
    }
    
    public void addExpense() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.next();
            System.out.print("Enter category: ");
            String category = scanner.next();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            
            expenses.add(new Expense(date, category, amount));
            saveExpenses();
            System.out.println("Expense added successfully.");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine();
        }
    }
    
    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        expenses.sort(Comparator.comparing(Expense::getDate));
        expenses.forEach(System.out::println);
    }
    
    public void categoryWiseTotal() {
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategory(), categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        categoryTotals.forEach((category, total) -> System.out.println(category + ": " + total));
    }
    
    public void menu() {
        while (true) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Add Expense");
            System.out.println("4. View Expenses");
            System.out.println("5. View Category-wise Total");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: registerUser(); break;
                case 2: if (loginUser()) menu(); break;
                case 3: addExpense(); break;
                case 4: viewExpenses(); break;
                case 5: categoryWiseTotal(); break;
                case 6: System.out.println("Exiting..."); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    public static void main(String[] args) {
    	ExpenseTrackerWithSimpleUI tracker = new ExpenseTrackerWithSimpleUI();
        tracker.menu();
    }
}
