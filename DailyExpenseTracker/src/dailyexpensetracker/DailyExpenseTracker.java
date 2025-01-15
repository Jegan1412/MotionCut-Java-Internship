package dailyexpensetracker;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Expense Class
class Expense {
    private int id;
    private LocalDate date;
    private double amount;
    private String category;
    private String description;

    public Expense(int id, LocalDate date, double amount, String category, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Date: " + date + ", Amount: " + amount + 
               ", Category: " + category + ", Description: " + description;
    }

    public String toFileFormat() {
        return id + "," + date + "," + amount + "," + category + "," + description;
    }

    public static Expense fromFileFormat(String line) {
        String[] parts = line.split(",");
        return new Expense(
                Integer.parseInt(parts[0]),
                LocalDate.parse(parts[1]),
                Double.parseDouble(parts[2]),
                parts[3],
                parts[4]
        );
    }
}

// ExpenseManager Class
class ExpenseManager {
    private List<Expense> expenses;
    private int expenseCounter;
    private final String filePath = "expenses.txt";

    public ExpenseManager() {
        this.expenses = new ArrayList<>();
        this.expenseCounter = 1;
        loadFromFile();
    }

    public void addExpense(LocalDate date, double amount, String category, String description) {
        Expense expense = new Expense(expenseCounter++, date, amount, category, description);
        expenses.add(expense);
        System.out.println("Expense added successfully.");
    }

    public void viewDailySummary(LocalDate date) {
        List<Expense> dailyExpenses = expenses.stream()
                .filter(e -> e.getDate().equals(date))
                .collect(Collectors.toList());
        displayExpenses(dailyExpenses);
    }

    public void viewWeeklySummary(LocalDate startDate, LocalDate endDate) {
        List<Expense> weeklyExpenses = expenses.stream()
                .filter(e -> !e.getDate().isBefore(startDate) && !e.getDate().isAfter(endDate))
                .collect(Collectors.toList());
        displayExpenses(weeklyExpenses);
    }

    public void viewMonthlySummary(int month, int year) {
        List<Expense> monthlyExpenses = expenses.stream()
                .filter(e -> e.getDate().getYear() == year && e.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
        displayExpenses(monthlyExpenses);
    }

    private void displayExpenses(List<Expense> expenseList) {
        if (expenseList.isEmpty()) {
            System.out.println("No expenses found for the specified period.");
            return;
        }
        double total = 0;
        for (Expense expense : expenseList) {
            System.out.println(expense);
            total += expense.getAmount();
        }
        System.out.println("Total Expenses: " + total);
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Expense expense : expenses) {
                writer.write(expense.toFileFormat());
                writer.newLine();
            }
            System.out.println("Expenses saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Expense expense = Expense.fromFileFormat(line);
                expenses.add(expense);
                expenseCounter = Math.max(expenseCounter, expense.getId() + 1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous expense records found.");
        } catch (IOException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }
}

// Main Class
public class DailyExpenseTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ExpenseManager expenseManager = new ExpenseManager();

    public static void main(String[] args) {
        System.out.println("Welcome to Daily Expense Tracker!");
        int choice;
        do {
            displayMenu();
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addExpense();
                case 2 -> viewDailySummary();
                case 3 -> viewWeeklySummary();
                case 4 -> viewMonthlySummary();
                case 5 -> expenseManager.saveToFile();
                case 6 -> System.out.println("Exiting the application. Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add Expense");
        System.out.println("2. View Daily Summary");
        System.out.println("3. View Weekly Summary");
        System.out.println("4. View Monthly Summary");
        System.out.println("5. Save Expenses");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addExpense() {
        System.out.print("Enter date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        expenseManager.addExpense(date, amount, category, description);
    }

    private static void viewDailySummary() {
        System.out.print("Enter date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        expenseManager.viewDailySummary(date);
    }

    private static void viewWeeklySummary() {
        System.out.print("Enter start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        expenseManager.viewWeeklySummary(startDate, endDate);
    }

    private static void viewMonthlySummary() {
        System.out.print("Enter month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine());
        expenseManager.viewMonthlySummary(month, year);
    }
}