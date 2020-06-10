
public class Main {

    private static Manager[] Managers;
    private static Employee[] Employees;
    private static Customer[] Customers;

    public static void main(String[] args){
        // Creates an array for 10 BabyGeese threads
        Managers = new Manager[10];
        Employees = new Employee[10];
        Customers = new Customer[10];

        // Creates the BabyGeese threads
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
}
