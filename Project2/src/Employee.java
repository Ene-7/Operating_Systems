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

            try {
                Store.MUTEX.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Store.CHECKOUT_REGISTER.acquire(); // Wait at Register for customer to come and pay.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msg("Hey " + Store.CurrentCustomer + " come to my register [Register: " + Number + "]");

            Store.MUTEX.release();

        } while (Store.CustomerOutCount != Store.NumCustomers);

        msg("I'm done for the day!");

    }

    //Message method suggested and provided by the assignment.
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }
}
