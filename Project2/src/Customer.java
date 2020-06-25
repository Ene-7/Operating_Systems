
public class Customer implements Runnable {
    private String Name; // The name of the Customer.
    private boolean isElder = false; // Is this Customer old or not. This will help determine if they can get priority self checkout from other customers.
    private String Number;
    private Thread CustomerThread; // The thread.

    Customer(String Num, int Elder_Chance){
        setName("Customer_" + Num);
        this.Number = Num; // Hold the Number value of customer will help when we have to exit in order
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


        // Wait for Store to Open:

        try {
            Store.STORE_IS_OPEN_SEMAPHORE.acquire(); // Wait until Manager notifies the store is open.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Store.STORE_IS_OPEN_SEMAPHORE.release(); // release semaphore for the next customer to know the store is open. This will help keep them in order of arrival.


        // GROUP CUSTOMERS:

        try {
            Store.MUTEX.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // P(Mutex), this will keep the counter variable safe.

        Store.CustomerInCount++;
        if(Store.CustomerInCount == Store.NumCustomers) {
            Store.MANAGER_WORK.release(); // If the last person forms a group then the Manager is done with his job and can go home.
        }
        if(Store.CustomerInCount % Store.Store_Capacity == 0 || Store.CustomerInCount == Store.NumCustomers){ // If the group has been form or we're the last one in can't form a full group
            Store.MUTEX.release(); // release the mutex for the other customers V(MUTEX);

            //This will block any other groups from coming in the store until the preceding group has finished shopping and have left the store.
            try {
                Store.GROUP_IN_SESSION.acquire(); // The first group won't block because semaphore is initialized to 1.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i = 1; i < Store.Store_Capacity; i++){
                Store.STORE_CAPACITY_GROUP.release(); //Last one to join group releases everyone that's waiting in the group to go in the store.
            }
        }
        else{
            Store.MUTEX.release(); // release the mutex for the next customer thread.
            try {
                Store.STORE_CAPACITY_GROUP.acquire(); // Block until group of 6 is formed then proceed.
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


        //CHECKOUT SECTION:

        msg("I got what I needed. Time to head to the checkout!");

        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); //Sleep for 2 to 5 Seconds to simulate rushing to the checkout. This was required for project one not sure if it's supposed
        } catch (InterruptedException e) {                         //to be part of project 2 as well.
            e.printStackTrace();
        }


        try {
            Store.MUTEX.acquire(); //Use Mutex to keep ME satisfied
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Store.CurrentCustomer = this.Name; // Tells the name to the Store so the Employee can call you over when they're free to take another customer. We need some way for the Employee to know who's supposed to be called next
        // I can't think of a better way...

        if(this.isElder){
            Store.ELDER_CHECKOUT_IN.release(); // Allow the Employee to serve you.
            Store.MUTEX.release();
            try {
                Store.ELDER_CHECKOUT_PAY.acquire(); // Wait until you've paid or done getting your stuff in the grocery bags or what not this can't just happen instantly
                msg("Finally bought my stuff.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // IF ELDER

        else { //Else if this is a normal Customer (Not Elderly), then randomly decide where they should wait in line to pay for.
            //Remove Points for this, I am Randomly picking where (Which Employee to wait for) the Customer should wait for in line for the Register. I don't think this is what's requested but I don't know how else to handle this situation. Sorry.
            int pickRegister = 1; // Default Register to pick for a Normal Customer, if it happens to be the Elderly one then the customer must pick another one randomly to wait in.
            while(pickRegister == Store.ElderlyCheckoutNum) // Make sure not to pick the Elderly Register, because it's exclusively designated for them.
             pickRegister = Store.RandomInt(1, Store.NumRegisters);

            for(int i = 0; i < Store.REGISTER_AVAILABILITY.length; i++){ // Go through all the registers if it's the one that is picked Wait in it.
                if(pickRegister == i){
                    try {
                        Store.MUTEX.release();
                        Store.CHECKOUT_REGISTER.release(); // Release an employee
                        Store.REGISTER_AVAILABILITY[i].acquire(); // Block until the Employee is ready to serve.
                        msg("Finally bought my stuff.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }





        try {
            this.CustomerThread.sleep(Store.RandomInt(2000,5000)); // Sleep for 2 to 5 seconds to simulate paying time.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //It is necessary to do this because if they pay instantly then every customer only uses register 1.

        try {
            Store.MUTEX.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Store.CustomerOutCount++; // CS must be protected by MUTEX semaphore.
        if(Store.CustomerOutCount % Store.Store_Capacity == 0){ // If this is the last person of the group that is currently shopping Release the other people to allow them to form a group.
            //This won't release if the last group of people is under the Store Capacity requirement but it doesn't matter because there will be no other customers that need to be released anyway
            Store.GROUP_IN_SESSION.release(); // Release the Waiting Group and allow them to come in to shop
        }
        msg("Got what I needed. Uh oh there's a traffic jam outside I can't leave!!!");
        Store.MUTEX.release();





        // Wait for employee to permit exit
        try {
            Store.CUSTOMER_EXIT_SEMAPHORE[Integer.parseInt(this.Number)-1].acquire(); // Block until Allowed to Leave im Order by an Employee. I subtract 1 because Customers are not French Counted.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("=== HAS LEFT THE PARKING LOT ===");
    }


    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        if(this.isElder) // I should have added this on the first project. It would have helped notice the Elderly but I've done so for this one at least. :^)
            System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName() +": "+m +" [THIS CUSTOMER IS AN ELDER]");
        else
            System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }

}
