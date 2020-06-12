
public class Customer implements Runnable {
    private String Name;
    private int Number; //will be useful when the customers needs to leave in order of their number.
    private boolean isElder = false; // Is this Customer old or not. This will help determine if they can get priority self checkout from other customers.
    private Thread CustomerThread;

    //Constructor for Customer Based off BabyGeese and MotherGoose Examples.
    Customer(String Num, int Elder_Chance){
        setName("Customer_" + Num);
        setNumber(Integer.parseInt(Num));
        this.CustomerThread = new Thread(this, Name);
        if(Elder_Chance == 4) this.isElder = true;
        // The assignment says that about 25% of the customers are elderly,
        // so using my Random function I roll a number from 1 to 4 each has a 25% chance, for each 4 rolled the customer will become an elder.
    }

    public void setName(String in){
        this.Name = in;
    }

    public void setNumber(int in){
        this.Number = in;
    }

    public String getName(){
        return this.Name;
    }

    //Called in Main to start the Thread.
    public void start() {
        CustomerThread.start();
    }

    @Override
    public void run() {
        try {
            this.CustomerThread.sleep(Store.RandomInt(10,1000));
            // This will simulate an arrival time from 1 to 1000 milliseconds.
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("I've arrived at the stores' parking lot, and I'm now in the queue waiting to go in.");
        Store.CUSTOMER_QUEUE.add(this); // Customer Object added to the Queue Located in Main.

        //TODO: Add another busy wait if the store is still not open, need to wait for Manager and employees to show up first.g
        while(!Store.STORE_IS_OPEN.get()){
            // Busy wait until store opens. Manager must open up the store, once there are enough people waiting.
        }

        //TODO: Busy Wait until store has space available to allow 6 customers inside to shop.
        // Busy Wait code here... Make sure they get in a FCFS order. This section Should be done by the code below:


        Customer ME = Store.CUSTOMER_QUEUE.poll(); // Identify the First Most Customer To Ensure First Come First Serve Order

        while(Store.CUSTOMERS_SHOPPING.get() == Store.Store_Capacity || !ME.Name.equals(this.Name)){
            // Busy Wait if there are 6 people shopping in the store exit once there is space.
        }
        Store.CUSTOMER_QUEUE.remove(ME); //Remove the one identified from the queue.
        Store.CUSTOMERS_SHOPPING.getAndIncrement(); // Add 1 to the Counter of shoppers to prevent others from coming in if there's 6 inside.

        msg("I'm finally inside and can shop. I better stay away from others, they could be sick!");

        //Simulate Shop Time
        try {
            this.CustomerThread.sleep(Store.RandomInt(200,1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }

}
