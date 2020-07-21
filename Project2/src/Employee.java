public class Employee implements Runnable {
    private String name;
    private String Number;
    private Thread EmployeeThread;

    Employee(String Num){
       setName("Employee_" + Num);
       this.Number = Num;
       this.EmployeeThread = new Thread(this, name);
    }

    public void setName(String in){
        this.name = in;
    }

    public Thread getThread(){
        return this.EmployeeThread;
    }

    public String getName(){
        return this.name;
    }

    public void start() {
        EmployeeThread.start();
    }

    @Override
    public void run() {
        try {
            this.EmployeeThread.sleep(Store.RandomInt(3000,6000)); //3 seconds to 6 second sleep
            // Random Sleep Time to simulate Arrival To Work.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("I've made it to the store, and I'm ready to work!");

        Store.WAIT_FOR_EMPLOYEES.release(); // will release the semaphore and eventually once all employees do this, the manager will open the store because everyone came to work.

        do {

            if(Integer.parseInt(this.Number) == Store.ElderlyCheckoutNum){
                //If this is the Employee assigned to the Elderly-only checkout, then serve only the Elderly Semaphore.
                //ElderlyCheckoutNum is randomly determined between a range of 1 to numRegisters. I've done this so it can work for any sizes of numRegisters should it need to change.
                //Also I assign the Employee who's number matches the Register chosen to serve the Elderly. I think it makes sense to do so :)

                try {
                    Store.ELDER_CHECKOUT_IN.acquire(); // Wait until an Elderly customer shows up to pay.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Store.MUTEX2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                msg("Hey " + Store.CurrentCustomer + " come to my register [Register: " + this.Number + "]"); // this should be seen as a CS because the CurrentCustomer (Name of Customer) is shared...

                try {
                    this.EmployeeThread.sleep(Store.RandomInt(3000,5000)); // Random time of packing bags & receiving payment.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Store.ELDER_CHECKOUT_PAY.release(); // Release the Elderly Customer because they've now paid and they can now leave.
                //Store.MUTEX2.release();

            } // IF ELDER


            else { // Else, the this is the other Employees working the regular Checkout registers and must serve regular people.

                try {
                    Store.CHECKOUT_REGISTER.acquire(); // Wait at Register for customer to come and pay.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Store.MUTEX2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                msg("Hey " + Store.CurrentCustomer + " come to my register [Register: " + Number + "]"); // this should be seen as a CS because the CurrentCustomer (Name of Customer) is shared...

                for(int i = 0; i < Store.REGISTER_AVAILABILITY.length; i++){
                    Store.REGISTER_AVAILABILITY[i].release();
                }

                try {
                    this.EmployeeThread.sleep(Store.RandomInt(3000,5000)); // Random time of packing bags & receiving payment.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Store.MUTEX2.release();

            } // ELSE REGULAR CUSTOMER

        } while (Store.CustomerOutCount != Store.NumCustomers);

        //Wait until the Last Customer



        //TODO Counting Semaphore where the employee releases all the waiting customers in order. Use an array of semaphores?
        if(Integer.parseInt(this.Number) == Store.NumRegisters){ // if this is the last Employee, then let him resolve the traffic issue and allow everyone else to go home.
            for(int i = 0; i < Store.CUSTOMER_EXIT_SEMAPHORE.length; i++){
                Store.CUSTOMER_EXIT_SEMAPHORE[i].release();
            }
        }

        msg("I'm done for the day!");

    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }
}
