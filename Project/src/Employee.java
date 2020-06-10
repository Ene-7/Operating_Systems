

public class Employee implements Runnable {
    private String name;
    private Thread EmployeeThread;

    Employee(String Num){
       setName("Employee_" + Num);
        this.EmployeeThread = new Thread(this, name);
    }

    public void setName(String in){
        this.name = in;
    }

    public String getName(){
        return this.name;
    }

    public void start() {
        EmployeeThread.start();
    }

    @Override
    public void run() {

    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-Main.time)+"] "+getName()+": "+m);
    }
}
