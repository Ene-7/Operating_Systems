
public class Customer implements Runnable {
    private String Name; // The name of the Customer.
    private boolean isElder = false; // Is this Customer old or not. This will help determine if they can get priority self checkout from other customers.
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
        msg("I've arrived at the stores' parking lot, and I'm now in the queue waiting to go in.");

        try {
            Store.STORE_IS_OPEN_SEMAPHORE.acquire(); // Wait until Manager notifies the store is open.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Store.STORE_IS_OPEN_SEMAPHORE.release(); // release semaphore for the next customer to know the store is open. This will help keep them in order of arrival.

        try {
            Store.MUTEX.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // P(Mutex), this will keep the counter variable safe.

        Store.CustomerCount++;
        if(Store.CustomerCount % Store.Store_Capacity == 0 || Store.CustomerCount == Store.NumCustomers){ // If the group has been form or we're the last one in
            Store.MUTEX.release(); // release the mutex for the other customers
            for(int i = 1; i < Store.Store_Capacity; i++){
                Store.STORE_CAPACITY_ENTRY.release(); // Release everyone that's waiting in the group to go in the store.
            }
        }
        else{
            Store.MUTEX.release(); // release the mutex for the next customer thread.
            try {
                Store.STORE_CAPACITY_ENTRY.acquire(); // Block until group of 6 is formed then proceed.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        msg("I'm finally inside and can shop. I better stay away from others, they could be sick!");




        //Simulate Shop Time
        try {
            this.CustomerThread.sleep(Store.RandomInt(5000,10000)); // 5 to 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        // SELF CHECKOUT SECTION:

        msg("I got what I needed. Time to head to the checkout!");


        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); //Sleep for 2 to 5 Seconds to simulate rushing to the checkout
        } catch (InterruptedException e) {
            e.printStackTrace();
        }






        // WAITING TO BE DIRECTED TO A REGISTER BY THE EMPLOYEE:


        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); // Sleep for 2 to 5 seconds to simulate paying time.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //It is necessary to do this because if they pay instantly then every customer only uses register 1.

        //Traffic Jam Event, Sleep until Employee handles it
        msg("Got what I needed. Uh oh there's a traffic jam outside I can't leave!!!");


        // Wait for employee to permit exit.


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
