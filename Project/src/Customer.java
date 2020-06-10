

public class Customer implements Runnable {
    private String name;
    private Thread CustomerThread;

    Customer(String Num){
        this.name = "Customer_" + Num;
        this.CustomerThread = new Thread(this, name);
    }

    public void start() {
        CustomerThread.start();
    }

    @Override
    public void run() {

    }
}
