public class Employee implements Runnable {
    private String name;
    private Thread EmployeeThread;

    Employee(String Num){
       setName("Employee_" + Num);
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
        Store.EMPLOYEE_IS_HERE.set(true); // Employee has arrived to work.
        int count = Store.NumCustomers; // The Employee counts how many people he should expect for today.
        while(count > 0){ // Keep working until there's no more customers left needing to shop.
            Customer CHECKOUT_ME = Store.CUSTOMER_CHECKOUT_QUEUE.peek(); // Employee Identifies the first customer in line to checkout.
            if(CHECKOUT_ME != null){ // make sure it's not a null value (this will evaluate false in the beginning when the store opens, but as time passes there should be plenty of people ready to checkout).
                for(int i = 0; i < Store.CHECKOUT_REGISTERS.length; i++){
                    if(Store.CHECKOUT_REGISTERS[i] == null && !CHECKOUT_ME.isCalledToRegister()){
                        msg("Hey " + CHECKOUT_ME.getName() + " go to register: " + (i+1) + " please!");
                        Store.CHECKOUT_REGISTERS[i] = CHECKOUT_ME; // Assign the next customer to a register
                        CHECKOUT_ME.callToRegister(); // set its boolean to true to allow customer to exit busy wait
                        count--; // removes the serviced customer from the count.
                        break; // move on to the next customer because we don't want to assign one person to multiple checkout lanes.
                    }//if
                }//for
            }//if
        }//while

        while(Store.CUSTOMERS_SHOPPING.get() > 0){} //BW Until all are done shopping and have left the store.

        for(Customer K: Store.Customers){ // Join all Customer threads in their ID order.
            K.getThread().interrupt();
            try {
                if(K.getThread().isAlive())
                    K.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        msg("I'm done for the day!");

    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }
}
