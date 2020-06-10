

public class Customer implements Runnable {
    private String Name;
    private Thread CustomerThread;

    Customer(String Num){
        this.Name = "Customer_" + Num;
        this.CustomerThread = new Thread();
    }

    public void start() {
        CustomerThread.start();
    }

    @Override
    public void run() {

    }
}
