

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
        //"Randomly determine when the Manager opens the store"
        try {
            this.ManagerThread.sleep(Store.RandomTime(200, 800));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("The store is now open! Come in everyone. Remember to stay 6 feet apart!");
        Store.STORE_IS_OPEN.set(true); // Sets the store to open.


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
