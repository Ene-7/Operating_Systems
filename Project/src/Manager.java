

public class Manager implements Runnable{
    private String name;
    private Thread ManagerThread;

    Manager(String Num){
        this.name = "Manager_" + Num;
        this.ManagerThread = new Thread(this, name);
    }

    public void start() {
        ManagerThread.start();
    }

    @Override
    public void run() {

    }
}
