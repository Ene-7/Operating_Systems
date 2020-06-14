
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {
    private static Manager[] Managers;
    private static Employee[] Employees;
    private static Customer[] Customers;
    private static int NumCustomers; // Number of total customers that will be shopping, provided as an input argument
    public static final int Store_Capacity = 6; // How many people can shop at a time
    public static final int NumSelf_Checkout = 4; // Number of Self Checkout registers
    public static long time = System.currentTimeMillis(); // The start of the Main/Store Thread.
    public static ConcurrentLinkedQueue<Customer> CUSTOMER_QUEUE = new ConcurrentLinkedQueue<Customer>(); //Customers waiting to get in and shop. might need to revert this.
    public static ConcurrentLinkedQueue<Customer> CUSTOMER_CHECKOUT_QUEUE = new ConcurrentLinkedQueue<Customer>(); // Checkout Register Queue.
    public static AtomicInteger CUSTOMERS_SHOPPING = new AtomicInteger(0); // Number of Current Shoppers inside the store. Atomic Integer to keep it thread safe.
    public static AtomicBoolean STORE_IS_OPEN =  new AtomicBoolean(false); // Store Starts off Closed. Must be opened up by the Manager once he sees enough people lining up.
    public static AtomicBoolean EMPLOYEE_IS_HERE =  new AtomicBoolean(false); // Is the Employee at work yet? (Used in Manager class to open up store).

    public static void main(String[] args){
            try {
                NumCustomers = Integer.parseInt(args[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("Invalid Input. Input Should Be An Integer Value. Please Reconfigure The Arguments And Try Again.");
            } // As requested, the number of Customers will be specified by an argument value.

        Managers = new Manager[1]; // Only one manager is referenced in the assignment, and they will open up the store and leave after things get going.
        Employees = new Employee[1]; // Only one employee is created, the exact number of employees is not specified in the assignment but I assume it is one based on it's singular noun mentioned at the last paragraph where customers leave.
        Customers = new Customer[NumCustomers]; // Customer count will be determined by the input argument as requested.

        // Creates the threads
        for (int i = 0; i < Managers.length; i++) {
            Managers[i] = new Manager(Integer.toString(i+1));
        }
        for(int i = 0; i < Employees.length; i++){
            Employees[i] = new Employee(Integer.toString(i+1));
        }
        for(int i = 0; i < Customers.length; i++){
            Customers[i] = new Customer(Integer.toString(i+1), RandomInt(1,4));
        }

        // Starts the threads
        for (Manager manager : Managers) {
            manager.start();
        }

        for (Employee employee : Employees) {
            employee.start();
        }

        for (Customer customer : Customers) {
            customer.start();
        }
    }

    // Random method that will decide the sleep times for Customers,
    // placed in main/store if it's needed elsewhere globally.
    public static int RandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
