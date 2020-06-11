
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
            Thread.sleep(Main.RandomTime(1,120));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } // This will simulate an arrival time from 1 to 120 seconds (2 Minutes).
        msg("Has arrived at the stores' parking lot, and is now in the queue waiting to go in.");
        Main.CUSTOMER_QUEUE.add(this); // Customer Object added to the Queue Located in Main.

        //TODO: Busy Wait until store has space available to allow 6 customers inside to shop.
        // Busy Wait code here... Make sure they get in a FCFS order.

        while(Main.CUSTOMERS_SHOPPING.get() >= 6){
          continue; // Although this is not necessary I've placed it to keep IntelliJ from complaining.
          // Busy Wait if there are 6 people shopping in the store exit once there is space.
        }

        Customer ME = Main.CUSTOMER_QUEUE.peek(); // See who the very first one in line is.
        Main.CUSTOMER_QUEUE.remove(); // Remove them from the Queue that they were waiting for outside.
        Main.CUSTOMERS_SHOPPING.getAndIncrement(); // Add 1 to the Counter of shoppers to prevent others from coming in if there's 6 inside.

        ME.msg("I'm finally inside and can shop. I better stay away from others, they could be sick!");

    }


    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-Main.time)+"] "+getName()+": "+m);
    }

}
