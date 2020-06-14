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

        //TODO EMPLOYEE IS THE LAST ONE TO LEAVE THE STORE AND EVERYONE ELSE WILL THEN WAIT FOR HIM
        // TO RESOLVE THE TRAFFIC JAM IN THE PARKING LOT. THEN ALL OF THE CUSTOMERS WILL LEAVE IN SEQUENTIAL ORDER TO 1 TO N.


    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }
}
