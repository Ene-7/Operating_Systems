/*
*  @author Eneid Papa
*  Operating Systems CS 340
*  Summer 2020
*  Project 2
*/

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Store {
    public static Manager[] Managers; // This will contain the number of managers. The project only mentions one so the program is built to only work with one manager.
    public static Employee[] Employees; // This will contain the Employees. The number of Employees is now equivalent to the number of Registers (NumSelf_Checkout) it is initialized in main.
    public static Customer[] Customers; // Customers will be specified by the command line argument and this array will be sized accordingly.
    public static int NumCustomers; // Number of total customers that will be shopping, provided as an input argument
    public static final int Store_Capacity = 6; // How many people can shop at a time
    public static final int NumRegisters = 3; // Number of Self Checkout registers
    public static long time = System.currentTimeMillis(); // The start time of the Heavyweight Main/Store Thread.

    public static final Semaphore STORE_IS_OPEN_SEMAPHORE = new Semaphore(1, true); // Binary Semaphore that will block all customers from entering the store if it's closed.
    public static final Semaphore MUTEX = new Semaphore(1, true); // Binary Semaphore To ensure no load and store issues arise for any counters. Usage of volatile variables is not permitted, so a mutex is necessary.
    public static final Semaphore STORE_CAPACITY_GROUP = new Semaphore(0, true); // Counting Semaphore that will allow customer to enter in groups of size Store Capacity.
    public static final Semaphore WAIT_FOR_EMPLOYEES = new Semaphore(0, true); // Counting Semaphore that will be used by Manager to wait and will be released by all the Employees.
    public static final Semaphore GROUP_IN_SESSION = new Semaphore(1, true); // Binary Semaphore that will Hold other customers from forming groups until the preceding group is done.
    public static final Semaphore MANAGER_WORK = new Semaphore(0, true); // Semaphore for Manager to go home after everyone has formed a group to go inside the store.
    public static final Semaphore CHECKOUT_REGISTER = new Semaphore(0, true); // Counting Semaphore for the Checkout.
    public static final Semaphore MUTEX2 = new Semaphore(1, true); // Binary Semaphore To ensure no load and store issues arise for any counters. Usage of volatile variables is not permitted, so a mutex is necessary.
    public static final Semaphore ELDER_CHECKOUT_IN = new Semaphore(0, true); // Binary Semaphore that will make the Employee wait until an elderly person arrives to pay.
    public static final Semaphore ELDER_CHECKOUT_PAY = new Semaphore(1, true); // Binary Semaphore that will make the Employee wait until an elderly person arrives to pay.

    public static boolean[] Register_Availability = new boolean[NumRegisters]; // This will be used to check if the current Register is available, and will be made thread safe using a mutex semaphore.
    public static String CurrentCustomer;
    public static final int ElderlyCheckoutNum = RandomInt(1, NumRegisters); // A random register is picked to be designated for only the Elderly. I do this so I don't hard code a register and want this to work for different checkout sizes.
    public static int CustomerInCount = 0; // Count the customers that are going in.
    public static int CustomerOutCount = 0; // Count the customers that are going in.

    public static void main(String[] args){
            try {
                NumCustomers = Integer.parseInt(args[0]); // interpret the Number from command line and check if it's a valid entry.
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("Invalid Input. Input Should Be An Integer Value. Please Reconfigure The Arguments And Try Again.");
            } // As requested, the number of Customers will be specified by an argument value.

        System.out.println("General Information:"+
                "\nNumber of Customers: " + NumCustomers+
                "\nNumber of Employees & Registers: " + NumRegisters +
                "\nStore Capacity: "+ Store_Capacity +
                "\nDesignated Register Number for the Elderly: " + ElderlyCheckoutNum + " (Meaning the elderly and only them will use Register " + ElderlyCheckoutNum + ")" +
                "\nThis is just general information about the initializing variables for the program. (Helps me keep track)");

        Managers = new Manager[1]; // Only one manager is referenced in the assignment, and they will open up the store and leave after there's no more customers in queue to enter.
        Employees = new Employee[NumRegisters]; // For Project 2 we are told that we need as many Employees as we have registers.
        Customers = new Customer[NumCustomers]; // Customer count will be determined by the input argument as requested.
        try {
            STORE_IS_OPEN_SEMAPHORE.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Creates the threads
        // Although Manager and Employee(s) do not need a for loop for initialization. I'm keeping them this way just in case their count scales. Program would have to be altered if this happens though.
        // the local for loop variable i  is incremented by 1 when constructing each class because the assignment requests that all threads must be named from 1 to N.
        for (int i = 0; i < Managers.length; i++) {
            Managers[i] = new Manager(Integer.toString(i+1));
        }
        for(int i = 0; i < Employees.length; i++){
            Employees[i] = new Employee(Integer.toString(i+1));
        }
        for(int i = 0; i < Customers.length; i++){
            Customers[i] = new Customer(Integer.toString(i+1), RandomInt(1,4)); // Will Create NumCustomers, along with a 25% probability of them being Elderly.
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

    // Random method that will decide the sleep times for Customers, Managers and Employees.
    // placed in main/store because it's needed elsewhere globally.
    public static int RandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
