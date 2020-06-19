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


        msg("I'm done for the day!");

    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }
}
