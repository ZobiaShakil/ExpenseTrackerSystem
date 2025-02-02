import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class Expense {
    int amount;
    String category;
    String date;
    String description;

    public Expense(int amount, String category, String date, String description) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Amount: " + amount + ", Category: " + category + ", Date: " + date + ", Description: " + description;
    }
}

 class ExpenseTracker{
    private List<Expense> expenses = new ArrayList<>();
    private Stack<Expense> undoStack = new Stack<>();
    private Queue<Expense> reimbursementQueue = new LinkedList<>();
    private Map<String, List<Expense>> expensesByCategory = new HashMap<>();
    private Map<String, List<Expense>> expensesByDate = new HashMap<>();
    private List<String> userDefinedCategories = new ArrayList<>(); // Changed from Set to List

    // Add a new expense
    public void addExpense(int amount, String category, String date, String description) {
        if (!userDefinedCategories.contains(category.toLowerCase())) {
            System.out.println("Invalid category. Available categories are: " + userDefinedCategories);
            return;
        }

        // Validate date format
        if (!isValidDate(date)) {
            System.out.println("Invalid date format. Please enter a valid date in the format YYYY-MM-DD.");
            return;
        }

        Expense expense = new Expense(amount, category, date, description);
        expenses.add(expense);
        undoStack.push(expense);

        // Update category map
        expensesByCategory.putIfAbsent(category, new ArrayList<>());
        expensesByCategory.get(category).add(expense);

        // Update date map
        expensesByDate.putIfAbsent(date, new ArrayList<>());
        expensesByDate.get(date).add(expense);

        System.out.println("Expense added successfully.");
    }

    // Validate if the date is in correct format and a valid date
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);  // Set lenient to false to make it strict
        try {
            sdf.parse(date);  // If parsing is successful, it's a valid date
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // View user-defined categories
    public void viewCategories() {
        System.out.println("Available categories: " + userDefinedCategories);
    }

    // Undo the last action
    public void undoLastAction() {
        if (!undoStack.isEmpty()) {
            Expense lastExpense = undoStack.pop();
            expenses.remove(lastExpense);
            expensesByCategory.get(lastExpense.category).remove(lastExpense);
            expensesByDate.get(lastExpense.date).remove(lastExpense);
            System.out.println("Last action undone: " + lastExpense);
        } else {
            System.out.println("No actions to undo.");
        }
    }

    // View all expenses
    public void displayAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            System.out.println("All recorded expenses:");
            expenses.forEach(System.out::println);
        }
    

    }
    // Main method to run the program
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        // Initialize user-defined categories
        System.out.print("How many categories do you want to create? ");
        int categoryCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter category names:");
        for (int i = 0; i < categoryCount; i++) {
            System.out.print("Category " + (i + 1) + ": ");
            String category = scanner.nextLine().toLowerCase();
            tracker.userDefinedCategories.add(category); // Adding to List
        }

        System.out.println("Categories created successfully!");

        while (true) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. Undo Last Action");
            System.out.println("3. View All Categories");
            System.out.println("4. View All Expenses");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    int amount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Category input with validation
                    String category;
                    while (true) {
                        System.out.println("Available categories: " + tracker.userDefinedCategories);
                        System.out.print("Enter category: ");
                        category = scanner.nextLine().toLowerCase();
                        if (tracker.userDefinedCategories.contains(category)) {
                            break;
                        } else {
                            System.out.println("Invalid category. Available categories are: " + tracker.userDefinedCategories);
                        }
                    }

                    // Date input with validation
                    String date;
                    while (true) {
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        date = scanner.nextLine();
                        if (tracker.isValidDate(date)) {
                            break;
                        } else {
                            System.out.println("Invalid date format. Please enter a valid date in the format YYYY-MM-DD.");
                        }
                    }

                    // Description input
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();

                    // Add the expense
                    tracker.addExpense(amount, category, date, description);
                    break;

                case 2:
                    tracker.undoLastAction();
                    break;

                case 3:
                    tracker.viewCategories();
                    break;

                case 4:
                    tracker.displayAllExpenses();
                    break;

                case 5:
                    System.out.println("Exiting the Expense Tracker. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

