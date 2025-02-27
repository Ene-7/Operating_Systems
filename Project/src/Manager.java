public class Manager implements Runnable{
    private String name;
    private Thread ManagerThread;

    Manager(String Num){
        setName("Manager_" + Num);
        this.ManagerThread = new Thread(this, name);
    }

    public void start() {
        ManagerThread.start();
    }

    @Override
    public void run() {
        //"Randomly determine when the Manager opens the store" - As requested by the project description:
        try {
            this.ManagerThread.sleep(Store.RandomInt(2000, 4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("I've made it to work. Let me wait until my Employee is here so I can open the store.");
        while(!Store.EMPLOYEE_IS_HERE.get()){
            //Busy Wait, Can't open the store until the employee comes to work.
        }

        msg("The store is now open! Come in everyone. Remember to stay 6 feet apart!");
        Store.STORE_IS_OPEN.set(true); // Sets the store to open.

        // MANAGER CAN ONLY LEAVE ONCE THERE ARE NO MORE CUSTOMERS WAITING OUTSIDE TO GET IN
        // ONCE THE CUSTOMER_QUEUE IS EMPTY MANAGER HEADS HOME.
        while(!Store.CUSTOMER_QUEUE.isEmpty()){
            // BW (Keep assisting the Customers entering the store until none are left)
        }
        msg("Looks like that's all of them. I'm heading home!");
    }

    public void setName(String in){
        this.name = in;
    }

    public String getName(){
        return this.name;
    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }

}
