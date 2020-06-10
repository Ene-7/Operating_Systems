

public class Employee implements Runnable {
    private String name;
    private Thread EmployeeThread;

    Employee(String Num){
        this.name = "Employee_" + Num;
        this.EmployeeThread = new Thread(this, name);
    }

    public void start() {
        EmployeeThread.start();
    }

    @Override
    public void run() {

    }
}
