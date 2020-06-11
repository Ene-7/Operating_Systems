import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger; // Not sure If these two are needed...

public class Main {
    private static Manager[] Managers;
    private static Employee[] Employees;
    private static Customer[] Customers;
    public static int NumCustomers;
    public static int Store_Capacity = 6;
    public static int NumSelf_Checkout = 4;
    public static long time = System.currentTimeMillis();
    public static Queue<Customer> CUSTOMER_QUEUE = new LinkedList<>();
    public static AtomicInteger CUSTOMERS_SHOPPING = new AtomicInteger(0);

    public static void main(String[] args){
            try {
                NumCustomers = Integer.parseInt(args[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("Invalid Input. Input Should Be An Integer Value. Please Reconfigure The Arguments And Try Again.");
            } // As requested, the number of Customers will be specified by an argument value.

        // Creates an array for 10 BabyGeese threads
        Managers = new Manager[1]; // review count
        Employees = new Employee[3]; //review count
        Customers = new Customer[NumCustomers];

        // Creates the threads
        for (int i = 0; i < Managers.length; i++) {
            Managers[i] = new Manager(Integer.toString(i));
        }
        for(int i = 0; i < Employees.length; i++){
            Employees[i] = new Employee(Integer.toString(i));
        }
        for(int i = 0; i < Customers.length; i++){
            Customers[i] = new Customer(Integer.toString(i));
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
    // placed in main if it's needed elsewhere globally.
    public static int RandomTime(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
