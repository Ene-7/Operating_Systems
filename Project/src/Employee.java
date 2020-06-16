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

    public String getName(){
        return this.name;
    }

    public void start() {
        EmployeeThread.start();
    }

    @Override
    public void run() {
        try {
            this.EmployeeThread.sleep(Store.RandomInt(1000,1200));
            // Random Sleep Time to simulate Arrival To Work.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("I've made it to the store, and I'm ready to work!");
        Store.EMPLOYEE_IS_HERE.set(true);
        int count = Store.NumCustomers; // The Employee counts how many people he should expect for today.
        while(count > 0){ // Keep working until there's no more customers left needing to shop.
            Customer CHECKOUT_ME = Store.CUSTOMER_CHECKOUT_QUEUE.peek(); // Employee Identifies the first customer in line to checkout.
            if(CHECKOUT_ME != null){ // make sure it's not a null value (this will evaluate false in the beginning when the store opens, but as time passes there should be plenty of people ready to checkout).
                for(int i = 0; i < Store.CHECKOUT_REGISTERS.length; i++){
                    if(Store.CHECKOUT_REGISTERS[i] == null && !CHECKOUT_ME.isCalledToRegister()){
                        msg("Hey " + CHECKOUT_ME.getName() + " go to register: " + (i+1) + " please!");
                        Store.CHECKOUT_REGISTERS[i] = CHECKOUT_ME;
                        CHECKOUT_ME.callToRegister(); // set its boolean to true to allow customer to exit busy wait
                        count--; // removes the serviced customer from the count.
                        break;
                    }
                }
            }
        }

        msg("I'm done for the day!");

        //TODO EMPLOYEE IS THE LAST ONE TO LEAVE THE STORE AND EVERYONE ELSE WILL THEN WAIT FOR HIM
        // TO RESOLVE THE TRAFFIC JAM IN THE PARKING LOT. THEN ALL OF THE CUSTOMERS WILL LEAVE IN SEQUENTIAL ORDER TO 1 TO N.


    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }
}
