

public class Customer implements Runnable {
    private String name;
    private Thread CustomerThread;

    //Constructor for Customer Based off BabyGeese and MotherGoose Examples.
    Customer(String Num){
        setName("Customer_" + Num);
        this.CustomerThread = new Thread(this, name);
    }


    public void setName(String in){
        this.name = in;
    }
    public String getName(){
        return this.name;
    }

    //Called in Main to start the Thread.
    public void start() {
        CustomerThread.start();
    }


    @Override
    public void run() {
        try {
            this.CustomerThread.sleep(Main.RandomTime(1,120));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } // This will simulate an arrival time from 1 to 120 seconds (2 Minutes).
        msg("Has arrived at the stores' parking lot, and is now in the queue waiting to go in.");
        Main.CUSTOMER_QUEUE.add(this); // Customer Object added to the Queue Located in Main.
        //TODO: Busy Wait until store has space available to allow 6 customers inside to shop.
        // Busy Wait code here... Make sure they get in a FCFS order.

        //TODO: Customer queue 6 at a time

    }


    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-Main.time)+"] "+getName()+": "+m);
    }

}
