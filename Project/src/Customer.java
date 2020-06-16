
public class Customer implements Runnable {
    private String Name;
    private int Number; //will be useful when the customers needs to leave in order of their number.
    private boolean isElder = false; // Is this Customer old or not. This will help determine if they can get priority self checkout from other customers.
    private boolean isCalled = false; // This will help for the checkout register for the
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

    public int getNumber(){
        return this.Number;
    }

    public String getName(){
        return this.Name;
    }

    public boolean isCalledToRegister(){
        return this.isCalled;
    }

    public void callToRegister(){
        this.isCalled = true;
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
        Store.CUSTOMER_QUEUE.add(this); // Customer Object added to the Queue Located in Main.
        msg("I've arrived at the stores' parking lot, and I'm now in the queue waiting to go in.");


        // busy wait if the store is still not open, need to wait for Manager and employees to show up first.
        while(!Store.STORE_IS_OPEN.get()){
            // Busy wait until store opens. Manager must open up the store, once there are enough people waiting.
        }

        // Busy Wait until store has space available to allow 6 customers inside to shop.
        // Make sure they get in a FCFS order.

        Customer ME = Store.CUSTOMER_QUEUE.peek(); // Identify the First Most Customer To Ensure First Come First Serve Order
        while(!ME.equals(this)){
            ME = Store.CUSTOMER_QUEUE.peek(); // Keep checking who's supposed to go, wait if it's not your turn
        }

        while (Store.CUSTOMERS_SHOPPING.get() == Store.Store_Capacity) {
            // Busy Wait if there are 6 people shopping in the store exit once there is space.
        }
        Store.CUSTOMERS_SHOPPING.getAndIncrement(); // Add 1 to the Counter of shoppers to prevent others from coming in if there's 6 inside.
        Store.CUSTOMER_QUEUE.remove(ME); //Remove the one identified from the queue.


        msg("I'm finally inside and can shop. I better stay away from others, they could be sick!");

        //Simulate Shop Time
        try {
            this.CustomerThread.sleep(Store.RandomInt(5000,10000)); // 5 to 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("I got what I needed. Time to head to the checkout!");
        this.CustomerThread.setPriority(7); // We increase the priority of the process to simulate the action of rushing to checkout
        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); //Sleep for 2 to 5 Seconds to simulate rushing to the checkout
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.CustomerThread.setPriority(5); // Set Priority back to DEFAULT Value.

        if(!this.isElder){ // If the current customer is not an Elderly Person, they should yield for the Elderly that may also be in line to a register.
            this.CustomerThread.yield();
            this.CustomerThread.yield();
        } // This should allow the elderly to get to the checkout first.
        //TODO ADD CHECKOUT STUFF HERE: WILL HAVE TO CALL IN EMPLOYEE SOMEHOW MAYBE ANOTHER ATOMIC BOOLEAN?


        Store.CUSTOMER_CHECKOUT_QUEUE.add(this); // Add this customer to the Checkout Queue
        Customer CHECKOUT_ME = Store.CUSTOMER_CHECKOUT_QUEUE.peek(); // Keep looking out on who's instructed to go next by the Store Employee.

        while(!this.isCalled){
            CHECKOUT_ME = Store.CUSTOMER_CHECKOUT_QUEUE.peek(); //If Customer has not been called on yet, keep looking out and wait for the Employee to direct them to a register.
            // Busy Wait until called into a register by Employee.
        }
        Store.CUSTOMER_CHECKOUT_QUEUE.remove(CHECKOUT_ME); // Walk out of the checkout queue and into the self paying area/register.
        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); // Sleep for 2 to 5 seconds to simulate paying time.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < Store.CHECKOUT_REGISTERS.length; i++){
            if(Store.CHECKOUT_REGISTERS[i] == this){
                Store.CHECKOUT_REGISTERS[i] = null; // Once the customer is done they open up the spot at the checkout for someone else to be called in to use it.
            }
        }
        Store.CUSTOMERS_SHOPPING.getAndDecrement(); //Remove the shopper form the store interior

        //Traffic Jam Event, BW until Employee handles it
        msg("Traffic! I hate it!");
    }


    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }

}
