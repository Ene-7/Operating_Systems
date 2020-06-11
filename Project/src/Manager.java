

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
        //TODO Fill in this with functions of Manager
        while(Store.CUSTOMER_QUEUE.size() != 6){
            //Todo Implement Thread wait for all Customers and Employees to leave.
        }
    }

    public void setName(String in){
        this.name = in;
    }

    public String getName(){
        return this.name;
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Store.time)+"] "+getName()+": "+m);
    }

}
