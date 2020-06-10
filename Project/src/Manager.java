

public class Manager implements Runnable{
    private String Name;
    private Thread ManagerThread;

    Manager(String Num){
        this.Name = "Manager_" + Num;
        this.ManagerThread = new Thread();
    }

    public void start() {
        ManagerThread.start();
    }

    @Override
    public void run() {

    }
}
