import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static Manager[] Managers;
    private static Employee[] Employees;
    private static Customer[] Customers;
    public static long time = System.currentTimeMillis();
    public Queue<Customer> CUSTOMER_QUEUE = new LinkedList<>();

    public static void main(String[] args){
        // Creates an array for 10 BabyGeese threads
        AtomicInteger Capacity;
        AtomicBoolean Flag;
        Managers = new Manager[10];
        Employees = new Employee[10];
        Customers = new Customer[10];

        // Creates the threads
        for (int i = 0; i < 10; i++) {
            Managers[i] = new Manager(Integer.toString(i));
            Employees[i] = new Employee(Integer.toString(i));
            Customers[i] = new Customer(Integer.toString(i));
        }

        // Starts the BabyGeese threads
        for (int i= 0; i < 10; i++) {
            Managers[i].start();
        }
    }


    // Random method that will decide the sleep times for Customers,
    // placed in main if it's needed elsewhere globally.
    public static int RandomTime(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
