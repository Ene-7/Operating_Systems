
public class Customer implements Runnable {
    private String Name; // The name of the Customer.
    private boolean isElder = false; // Is this Customer old or not. This will help determine if they can get priority self checkout from other customers.
    private boolean isCalled = false; // This will help for the checkout register and also for when customers need to leave
    private Thread CustomerThread; // The thread.

    Customer(String Num, int Elder_Chance){
        setName("Customer_" + Num);
        this.CustomerThread = new Thread(this, Name);
        if(Elder_Chance == 4) this.isElder = true;
        // The assignment says that about 25% of the customers are elderly,
        // so using my Random function I roll a number from 1 to 4 each has a 25% chance, for each 4 rolled the customer will become an elder.
    }

    public void setName(String in){
        this.Name = in;
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
            this.CustomerThread.sleep(Store.RandomInt(1000,10000));
            // This will simulate an arrival time from 1 to 10 seconds.
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Store.CUSTOMER_QUEUE.add(this); // Customer Object added to the Queue Located in Main.
        msg("I've arrived at the stores' parking lot, and I'm now in the queue waiting to go in.");


        try {
            Store.STORE_IS_OPEN_SEMAPHORE.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Store.STORE_IS_OPEN_SEMAPHORE.release();



        msg("I'm finally inside and can shop. I better stay away from others, they could be sick!");

        //Simulate Shop Time
        try {
            this.CustomerThread.sleep(Store.RandomInt(5000,10000)); // 5 to 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // SELF CHECKOUT SECTION:

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
        Store.CUSTOMER_CHECKOUT_QUEUE.add(this); // Add this customer to the Checkout Queue




        // WAITING TO BE DIRECTED TO A REGISTER BY THE EMPLOYEE:


        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); // Sleep for 2 to 5 seconds to simulate paying time.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //It is necessary to do this because if they pay instantly then every customer only uses register 1.

        //Traffic Jam Event, Sleep until Employee handles it
        msg("Got what I needed. Uh oh there's a traffic jam outside I can't leave!!!");
        //We now simulate the long traffic jam by doing a long sleep that will be interrupted by the Employee Thread.
        try {
            this.CustomerThread.sleep(Store.RandomInt(500000,1000000)); // will sleep for 500 to 1000 seconds
        } catch (InterruptedException e) {
            msg("INTERRUPT BY EMPLOYEE: We're saved! We can finally go home!");
        }
        msg("=== HAS LEFT THE PARKING LOT ===");
    }


    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        if(this.isElder)
            System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName() +": "+m +" [THIS CUSTOMER IS AN ELDER]");
        else
            System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }

}
