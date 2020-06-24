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

        msg("I've made it to work. Let me wait until my Employees are here here so I can open the store.");

        for(int i = 0; i < Store.NumSelf_Checkout; i++){ // Will do P(WAIT_FOR_EMPLOYEES) NumSelf_Checkout times because: NumSelf_Checkout = Employee count
            try {
                Store.WAIT_FOR_EMPLOYEES.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // This will release once all employees show up to work and do V(WAIT_FOR_EMPLOYEES);

        msg("The store is now open! Come in everyone. Remember to stay 6 feet apart!");

        Store.STORE_IS_OPEN_SEMAPHORE.release(); //opens the store for the customers to start coming in.

        // MANAGER CAN ONLY LEAVE ONCE THERE ARE NO MORE CUSTOMERS WAITING OUTSIDE TO GET IN



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
