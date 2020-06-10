

public class Employee implements Runnable {
    private String Name;
    private Thread EmployeeThread;

    Employee(String Num){
        this.Name = "Employee_" + Num;
        this.EmployeeThread = new Thread();
    }

    public void start() {
        EmployeeThread.start();
    }

    @Override
    public void run() {

    }
}
